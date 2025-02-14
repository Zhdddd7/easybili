package com.easybili.web;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(scanBasePackages = {"com.easybili"} )
@MapperScan("com.easybili")
public class EasybiliWebRunApplication {
    public static void main(String[] args){
        SpringApplication.run(EasybiliWebRunApplication.class, args);
    }

}
