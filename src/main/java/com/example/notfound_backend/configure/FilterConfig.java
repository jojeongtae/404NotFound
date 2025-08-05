package com.example.notfound_backend.configure;

import com.example.notfound_backend.component.PutMultipartFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<PutMultipartFilter> putMultipartFilter() {
        FilterRegistrationBean<PutMultipartFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new PutMultipartFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
