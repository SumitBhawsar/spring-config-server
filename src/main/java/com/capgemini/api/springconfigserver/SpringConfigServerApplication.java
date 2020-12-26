package com.capgemini.api.springconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableConfigServer
public class SpringConfigServerApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(SpringConfigServerApplication.class, args);
		}catch (Exception exc){
			exc.printStackTrace();
		}
	}
}
