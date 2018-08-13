package com.curchat.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {
		"com.curchat.controller",
		"com.curchat.config",
		"com.curchat.model",
		"com.curchat.repository",
		"com.curchat.service"
})
@EntityScan("com.curchat.model")
@EnableJpaRepositories("com.curchat.repository")
@EnableTransactionManagement
public class CurchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurchatApplication.class, args);
	}
}
