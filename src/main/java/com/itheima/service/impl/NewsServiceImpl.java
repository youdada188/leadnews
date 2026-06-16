package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.*;
import com.itheima.dto.ArticleHomeDto;
import com.itheima.dto.NewsDto;
import com.itheima.dto.NewsPageReqDto;
import com.itheima.entity.Material;
import com.itheima.entity.News;
import com.itheima.entity.NewsMaterial;
import com.itheima.entity.WmUser;
import com.itheima.mapper.MaterialMapper;
import com.itheima.mapper.NewsMapper;
import com.itheima.mapper.NewsMaterialMapper;
import com.itheima.mapper.WmUserMapper;
import com.itheima.service.NewsFreeMarkerService;
import com.itheima.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.utils.WmThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章信息表 服务实现类
 * </p>

 */
@Service
@Transactional
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private NewsMaterialMapper newsMaterialMapper;
    @Lazy
    @Autowired
    private NewsFreeMarkerService newsFreeMarkerService;
    /**
     * 发布文章或保存为草稿
     */
    @Override
    public ResponseResult submitNews(NewsDto dto) {
        //0.条件判断
        if(dto == null || dto.getContent() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //1.保存或修改文章
        News news = new News();
        //属性拷贝 属性名词和类型相同才能拷贝
        BeanUtils.copyProperties(dto,news);
        //封面图片  list---> string
        if(dto.getImages() != null && dto.getImages().size() > 0){
            String imageStr = StringUtils.join(dto.getImages(), ",");
            news.setImages(imageStr);
        }
        //如果当前封面类型为自动 -1
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            news.setType(null);
        }
        saveOrUpdateNews(news);
        //2.判断是否为草稿  如果为草稿结束当前方法
        if(dto.getStatus().equals(0)){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        //3.不是草稿，保存文章内容图片与素材的关系
        //获取到文章内容中的图片信息
        List<String> materials =  ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,news.getId());
        //4.不是草稿，保存文章封面图片与素材的关系，如果当前布局是自动，需要匹配封面图片
        saveRelativeInfoForCover(dto,news,materials);
        //异步调用 生成静态文件上传到MinIO中
        newsFreeMarkerService.buildArticleToMinIO(news,news.getContent());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }
    /**
     * 保存或修改文章
     */
    private void saveOrUpdateNews(News news) {
        //补全属性
        Integer id = WmThreadLocalUtil.getUser().getId();
        news.setAuthorId(id);
        WmUser user = wmUserMapper.selectById(id);
        news.setAuthorName(user.getNickname());
        news.setCreatedTime(new Date());
        news.setPublishTime(new Date());
        news.setStatus((byte)9);
        if(news.getId() == null){
            //保存文章
            save(news);
        }else {
            //修改文章
            //删除文章图片与素材的关系
            newsMaterialMapper.delete(Wrappers.<NewsMaterial>lambdaQuery().eq(NewsMaterial::getNewsId,news.getId()));
            updateById(news);
        }

    }
    /**
     * 提取文章内容中的图片信息
     */
    private List<String> ectractUrlInfo(String content) {
        List<String> materials = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if(map.get("type").equals("image")){
                String imgUrl = (String) map.get("value");
                materials.add(imgUrl);
            }
        }
        return materials;
    }
    /**
     * 处理文章内容图片与素材的关系
     */
    private void saveRelativeInfoForContent(List<String> materials, Long newsId) {
        saveRelativeInfo(materials,newsId,WemediaConstants.WM_CONTENT_REFERENCE);
    }
    /**
     * 保存文章图片与素材的关系到数据库中
     */
    private void saveRelativeInfo(List<String> materials, Long newsId, Short type) {
        if(materials != null && !materials.isEmpty()){
            //通过图片的url查询素材的id
            List<Material> dbMaterials = materialMapper.selectList(Wrappers.<Material>lambdaQuery().in(Material::getUrl, materials));
            //判断素材是否有效
            if(dbMaterials==null || dbMaterials.size() == 0){
                //手动抛出异常   第一个功能：能够提示调用者素材失效了，第二个功能，进行数据的回滚
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }
            if(materials.size() != dbMaterials.size()){
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }
            List<Integer> idList = dbMaterials.stream().map(Material::getId).collect(Collectors.toList());
            //批量保存
            newsMaterialMapper.saveRelations(idList,newsId,type);
        }
    }
    private void saveRelativeInfoForCover(NewsDto dto, News news, List<String> materials) {
        List<String> images = dto.getImages();

        //如果当前封面类型为自动，则设置封面类型的数据
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            //多图
            if(materials.size() >= 3){
                news.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            }else if(materials.size() >= 1 && materials.size() < 3){
                //单图
                news.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            }else {
                //无图
                news.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            //修改文章
            if(images != null && images.size() > 0){
                news.setImages(StringUtils.join(images,","));
            }
            updateById(news);
        }
        //第二个功能：保存封面图片与素材的关系
        if(images != null && images.size() > 0){
            saveRelativeInfo(images,news.getId(),WemediaConstants.WM_COVER_REFERENCE);
        }
    }
    /**
     * 条件查询文章列表
     */
    @Override
    public ResponseResult findList(NewsPageReqDto dto) {
        //1.检查参数
        dto.checkParam();
        //2.分页条件查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<News> lambdaQueryWrapper = new LambdaQueryWrapper();
        //状态精确查询
        if (dto.getStatus() != null) {
            lambdaQueryWrapper.eq(News::getStatus, dto.getStatus());
        }
        //频道精确查询
        if (dto.getChannelId() != null) {
            lambdaQueryWrapper.eq(News::getChannelId, dto.getChannelId());
        }
        //时间范围查询
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
            lambdaQueryWrapper.between(News::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());
        }
        //关键字的模糊查询
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            lambdaQueryWrapper.like(News::getTitle, dto.getKeyword());
        }
        //查询当前登录人的文章
        lambdaQueryWrapper.eq(News::getAuthorId, WmThreadLocalUtil.getUser().getId());
        //按照发布时间倒序查询
        lambdaQueryWrapper.orderByDesc(News::getPublishTime);
        page = page(page, lambdaQueryWrapper);
        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }
    @Autowired
    private NewsMapper newsMapper;
    /**
     * 加载文章列表
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto) {
        //1.检验参数
        //分页条数的校验
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }
        dto.setSize(size);
        //频道参数校验
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag("__all__");
        }
        //时间校验
        if(dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if(dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }
        Short type=1;
        //2.查询
        List<News> articleList = newsMapper.loadArticleList(dto,type);
        //3.结果返回
        return ResponseResult.okResult(articleList);
    }
}
