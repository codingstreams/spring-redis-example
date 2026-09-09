package com.example.springredisexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringRedisExampleApplication {

  static void main(String[] args) {
    SpringApplication.run(SpringRedisExampleApplication.class, args);
  }

}
