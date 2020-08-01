package com.tunion.dubbo.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created by Think on 2017/11/16.
 */
@EnableAutoConfiguration
public class ProviderStartApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProviderStartApplication.class,args);
    }
}
