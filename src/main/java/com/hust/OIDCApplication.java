package com.hust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "http://localhost:63342")
@ServletComponentScan//开启了对Servlet组件的支持
@SpringBootApplication//启动类
@MapperScan("com.hust.mapper")
public class OIDCApplication {

    public static void main(String[] args) {
        SpringApplication.run(OIDCApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
