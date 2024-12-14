package com.example.demo.jwt;

import ch.qos.logback.core.net.ObjectWriter;
import com.example.demo.Document.User;
import com.example.demo.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;


@Component
public class AuthJwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Value("${spring.public.urls}")
    String[] publicurls;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Triggered jwt");
        for(String s :publicurls){
            if (request.getRequestURI().startsWith(s)){
                System.out.println("Bypass jwt");
                filterChain.doFilter(request,response);
                return;
            }
        }
        String token= jwtUtils.getJwtfromHeader(request);
        if (token==null || !jwtUtils.validateJwt(token)){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            Map<String,String> map=new HashMap<>();
            map.put("status","Invalid or missing token");
            response.getWriter().write(new ObjectMapper().writeValueAsString(map));
            return;
        }
        Claims claims=jwtUtils.getclaims(token);
        System.out.println(claims);
        String type=(String) claims.get("type");
        if (type==null){
         return;
        }
        if (request.getRequestURI().startsWith("/web") && type.equals("api")){
            return;
        }
        if (request.getRequestURI().startsWith("/api") && type.equals("aud")){
            return;
        }
        String username=claims.getSubject();
        User principal = userDetailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(
                principal,null,principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        System.out.println("Authh");
        filterChain.doFilter(request,response);
    }
}
