package com.example.vuelog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "revi1337")
public class AppConfig {

    public String jwtKey;
}
