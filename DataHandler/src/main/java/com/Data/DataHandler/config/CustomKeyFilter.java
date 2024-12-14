package com.Data.DataHandler.config;

import com.Data.DataHandler.Service.FilterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Component
public class CustomKeyFilter extends OncePerRequestFilter {

    @Autowired
    FilterService filterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/addnode")){
            System.out.println("passed");
            filterChain.doFilter(request,response);
            return;
        }
        String auth= filterService.getKeyFromHeader(request);
        if (auth==null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            Map<String, String> map=new HashMap<>();
            map.put("Status","missing key");
            response.getWriter().write(new ObjectMapper().writeValueAsString(map));
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        System.out.println(auth);
        try{
            boolean valid =filterService.ValidateKey(auth);
            System.out.println(valid);
            if (!valid){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                Map<String, String> map=new HashMap<>();
                map.put("Status","Invalid key");
                response.getWriter().write(new ObjectMapper().writeValueAsString(map));
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            filterChain.doFilter(request,response);
        }
        catch (Exception e){
            System.out.println(e.toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            Map<String, String> map=new HashMap<>();
            map.put("Status","Invalid key");
            response.getWriter().write(new ObjectMapper().writeValueAsString(map));
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
