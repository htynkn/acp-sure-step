package com.huangyunkun.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.huangyunkun")
public class SureStepCliApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SureStepCliApplication.class, args)));
    }
}
