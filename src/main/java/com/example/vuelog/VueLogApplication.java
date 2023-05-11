package com.example.vuelog;

import com.example.vuelog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class VueLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueLogApplication.class, args);
	}

}
