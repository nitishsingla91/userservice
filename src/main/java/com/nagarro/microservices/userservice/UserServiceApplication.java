package com.nagarro.microservices.userservice;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@EnableFeignClients("com.nagarro.microservices.userservice")
@SpringBootApplication
@EnableAutoConfiguration
public class UserServiceApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	


}
