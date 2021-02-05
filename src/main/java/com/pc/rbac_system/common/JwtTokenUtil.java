package com.pc.rbac_system.common;

import com.pc.rbac_system.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;


@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "iat";

    @Value("${jwt.secret}")
    private String secert; //pccc
    @Value("${jwt.expiration}")
    private Long expiration;//过期时间 1800
    @Value("${jwt.tokenHead}")
    private String tokenHead;//bearer
    @Value("${jwt.tokenHeader}")
    private String tokenHeader; //Authorization

    public String generatedToken(User user){
        HashMap headMap =  new HashMap<>();
        headMap.put("typ",tokenHeader);
        headMap.put("cty",tokenHead);

        JwtBuilder jwtBuilder = Jwts.builder() //调用builder 创建jwt
                .setHeader(headMap) //设置头部信息
                .setSubject(user.getUsername()) // 设置sub为用户名
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000)) //设置过期时间
                .setIssuedAt(new Date(System.currentTimeMillis())) //设置创建时间
                .signWith(SignatureAlgorithm.HS512,secert); //设置加密方式，和秘钥（盐）
        return jwtBuilder.compact();
    }

    public String getUsernameFromToken(String token){
        String username = null;
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secert).parseClaimsJws(token).getBody();
        }catch (Exception e){
            Logger.getGlobal().info("jwt 解析失败");
        }
        try {
             username = claims.getSubject();
        }catch (Exception e){
            Logger.getGlobal().info("jwt sub无值");
        }
        return username;
    }


    public boolean validateToken(String token){
        Jws<Claims> claimsJws = null;
        claimsJws = Jwts.parser().setSigningKey(secert).parseClaimsJws(token);


        //校验token是否过期
        if (new Date(System.currentTimeMillis()).before(claimsJws.getBody().getExpiration())){
            return true;
        }
        return false;
    }

}
