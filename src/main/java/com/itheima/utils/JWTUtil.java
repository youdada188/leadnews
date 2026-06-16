package com.itheima.utils;

import io.jsonwebtoken.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class JWTUtil {

    // [Bug10修复] TOKEN的有效期为1小时（3600秒），原注释"一天"有误
    private static final int TOKEN_TIME_OUT = 3_600;
    // 加密KEY（Base64编码的字符串）
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    // 最小刷新间隔(S)
    private static final int REFRESH_TIME = 300;

    public static String getToken(Long id) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id", id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(currentTime))
                .setSubject("system")
                .setIssuer("heima")
                .setAudience("app")
                .compressWith(CompressionCodecs.GZIP)
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))
                .addClaims(claimMaps)
                .compact();
    }

    public static Jws<Claims> getJws(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token);
    }

    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    public static JwsHeader getHeaderBody(String token) {
        return getJws(token).getHeader();
    }

    public static int verifyToken(Claims claims) {
        if (claims == null) {
            return 1;
        }
        try {
            if ((claims.getExpiration().getTime() - System.currentTimeMillis()) > REFRESH_TIME * 1000) {
                return -1;
            } else {
                return 0;
            }
        } catch (ExpiredJwtException ex) {
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(TOKEN_ENCRY_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static void main(String[] args) {
        String token = JWTUtil.getToken(1102L);
        System.out.println("token:" + token);
        Jws<Claims> jws = JWTUtil.getJws(token);
        Claims claims = jws.getBody();
        int i = JWTUtil.verifyToken(claims);
        System.out.println("是否过期:" + i);
        System.out.println("解析后的id:" + claims.get("id"));
    }
}