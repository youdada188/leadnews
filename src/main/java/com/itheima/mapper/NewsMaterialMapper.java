package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.NewsMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface NewsMaterialMapper extends BaseMapper<NewsMaterial> {
     void saveRelations(@Param("materialIds") List<Integer> materialIds,@Param("newsId") Long newsId, @Param("type")Short type);
}