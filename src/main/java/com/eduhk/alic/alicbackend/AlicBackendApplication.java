package com.eduhk.alic.alicbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eduhk.alic.alicbackend.dao")
public class AlicBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlicBackendApplication.class, args);
    }

}
