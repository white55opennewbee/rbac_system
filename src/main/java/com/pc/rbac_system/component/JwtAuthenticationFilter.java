package com.pc.rbac_system.component;

import com.alibaba.fastjson.JSON;
import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.JwtTokenUtil;
import com.pc.rbac_system.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.tokenHead}")
    private String tokenHead;//bearer
    @Value("${jwt.tokenHeader}")
    private String tokenHeader; //Authorization
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String header = httpServletRequest.getHeader(tokenHeader);


        if (!StringUtils.isEmpty(header)) {
            String token = header.substring(tokenHead.length() + 1).trim();
            boolean b = false;
            try {
                b = jwtTokenUtil.validateToken(token);
            }catch (Exception e){
                httpServletResponse.setContentType("application/json;charset=utf-8");
                httpServletResponse.getWriter().print(JSON.toJSONString(Result.error(CodeMsg.USER_LOGIN_FAIL)));
                httpServletResponse.getWriter().flush();
                return;
            }
            if (b && SecurityContextHolder.getContext().getAuthentication() == null) {
                String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(httpServletRequest);
                    usernamePasswordAuthenticationToken.setDetails(details);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
