package com.tunion.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

/**
 * Created by Think on 2017/11/16.
 */
@SpringBootApplication(scanBasePackages = "com.tunion.dubbo")
@EnableAutoConfiguration(exclude={JpaRepositoriesAutoConfiguration.class})
public class ConsumerStartApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(ConsumerStartApplication.class);
        app.run(args);
    }
}
