package com.itheima.interceptor;

import com.itheima.entity.WmUser;
import com.itheima.utils.JWTUtil;
import com.itheima.utils.WmThreadLocalUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

public class WmTokenInterceptor implements HandlerInterceptor {

    // 白名单路径（无需token），使用精确匹配
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/login",
            "/register"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 白名单精确匹配放行
        if (WHITE_LIST.contains(uri)) {
            return true;
        }

        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"msg\":\"token过期或未传\",\"code\":401}");
            return false;
        }

        try {
            Claims claimsBody = JWTUtil.getJws(token).getBody();
            int result = JWTUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"msg\":\"token过期或未传\",\"code\":401}");
                return false;
            }

            Object userIdObj = claimsBody.get("id");
            Integer userId = null;
            if (userIdObj instanceof Integer) {
                userId = (Integer) userIdObj;
            } else if (userIdObj instanceof Long) {
                userId = ((Long) userIdObj).intValue();
            } else if (userIdObj instanceof String) {
                userId = Integer.parseInt((String) userIdObj);
            }
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            WmUser wmUser = new WmUser();
            wmUser.setId(userId);
            WmThreadLocalUtil.setUser(wmUser);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"msg\":\"token无效\",\"code\":401}");
            return false;
        }
        return true;
    }

    // postHandle 不再清理 ThreadLocal，改到 afterCompletion
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WmThreadLocalUtil.clear();
    }
}