package com.easybili.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${project.folder: }")
    private String projectFolder;
    @Value("${admin.account: }")
    private  String account;
    @Value("${admin.password")
    private  String password;
}
