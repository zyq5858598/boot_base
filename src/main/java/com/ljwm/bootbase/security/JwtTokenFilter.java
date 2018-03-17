package com.ljwm.bootbase.security;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : JWT Token全局过滤器
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenHead}")
    private String tokenPrefix;

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    /**
     * 核心认证方法
     *
     * @param request
     */
    private void getAuthFromRequest(HttpServletRequest request) {
        // 如果认证信息已经存在 不继续认证新的
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // 解析token
            String tokenAllStr = request.getHeader(this.header);
            if (StrUtil.isNotBlank(tokenAllStr) && tokenAllStr.startsWith(tokenPrefix)) {
                // 符合要求的token
                String token = tokenAllStr.substring(tokenPrefix.length());
                String account = JwtKit.getUsernameFormToken(token);
                if (StrUtil.isNotBlank(account)) {
                    // 从token中认证用户名
                    IJwtAndSecurityAble userDetails = (IJwtAndSecurityAble) userDetailsServiceImpl.loadUserByUsername(account);

                    if (JwtKit.validateToken(token, userDetails)) {
                        // 认证成功 构造Spring认证令牌
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 存入令牌
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    }

                }

            }
        }

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getAuthFromRequest(request);
        filterChain.doFilter(request, response);
    }


}
