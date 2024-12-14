package com.Data.DataHandler.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FIlterCOnfig {

    @Autowired
    CustomKeyFilter customKeyFilter;

    public FilterRegistrationBean<CustomKeyFilter> filterRegistrationBean(){
            FilterRegistrationBean<CustomKeyFilter> filter=new FilterRegistrationBean<>();
            filter.setFilter(customKeyFilter);
            filter.setOrder(1);
            filter.setUrlPatterns(List.of("/**"));
            return filter;
    }
}
