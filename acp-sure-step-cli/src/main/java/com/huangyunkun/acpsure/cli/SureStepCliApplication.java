package com.huangyunkun.acpsure.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.huangyunkun.acpsure.core")
public class SureStepCliApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SureStepCliApplication.class, args)));
    }
}
