package com.easybili.entities.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${project.folder: }")
    private String projectFolder;
    @Value("${admin.account: }")
    private  String adminAccount;
    @Value("${admin.password: }")
    private String adminPassword;
    @Value("${showFFmpegLog:true}")
    private  Boolean showFFmpeglog;

    @Value("${es.host.port:127.0.0.1:9200}")
    private String esHostPort;
    @Value("${es.index.video.name:easybili_video}")
    private  String esIndexVideoName;

}
