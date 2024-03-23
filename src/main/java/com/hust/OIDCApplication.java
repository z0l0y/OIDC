package com.hust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication//启动类
@MapperScan("com.hust.mapper")
public class OIDCApplication {

    public static void main(String[] args) {
        SpringApplication.run(OIDCApplication.class, args);
    }

}
