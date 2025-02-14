package com.easybili.admin;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(scanBasePackages = {"com.easybili"})
@MapperScan("com.easybili")
public class EasybiliAdminRunApplication {
    public static void main(String[] args){
        SpringApplication.run(EasybiliAdminRunApplication.class, args);
    }
}
