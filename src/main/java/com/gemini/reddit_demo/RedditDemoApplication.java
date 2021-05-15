package com.gemini.reddit_demo;


import com.gemini.reddit_demo.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class RedditDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditDemoApplication.class, args);
	}

}
