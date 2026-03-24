package com.huangyunkun.acpsure.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.huangyunkun.acpsure.core", "com.huangyunkun.acpsure.web"})
public class SureStepWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SureStepWebApplication.class, args);
    }
}
