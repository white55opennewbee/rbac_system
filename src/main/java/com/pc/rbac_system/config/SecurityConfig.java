package com.pc.rbac_system.config;

import com.alibaba.fastjson.JSON;
import com.pc.rbac_system.common.CodeMsg;
import com.pc.rbac_system.common.Result;
import com.pc.rbac_system.component.JwtAuthenticationFilter;
import com.pc.rbac_system.service.serviceImpl.MyUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.annotation.WebFilter;
import java.util.Collections;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors()
                .configurationSource(request->{             // 跨域问题 springboot 2.x后 origin 需要设置 setAllowedOriginPatterns 而不是 setAllowedOrigin
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
                    //2,允许任何请求头
                    corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
                    //3,允许任何方法
                    corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
                    //4,允许凭证
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                })
                .and().authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers("/common/**","/login","/register","/css/**","/js/**","/font/**", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**").permitAll().anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().userDetailsService(userDetailsService).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        http.exceptionHandling()                //设置权限认证错误handler，权限认证错误后续处理
                .accessDeniedHandler((req,resp,var3)->{
                    resp.setContentType("application/json;charset=utf-8");
                    resp.getWriter().write(JSON.toJSONString(Result.error(CodeMsg.USER_PERMISSION_ERROR)));
                    resp.getWriter().flush();
                })
                //设置登录错误handler，token认证失败后续处理
                .authenticationEntryPoint((req,resp,authenticationException)->{
                    resp.setContentType("application/json;charset=utf-8");
                    resp.getWriter().write(JSON.toJSONString(Result.error(CodeMsg.USER_LOGIN_FAIL)));
                    resp.getWriter().flush();
                });
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
