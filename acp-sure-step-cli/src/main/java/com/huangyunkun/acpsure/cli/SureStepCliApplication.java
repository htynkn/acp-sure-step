package com.huangyunkun.acpsure.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication(scanBasePackages = {"com.huangyunkun.acpsure.core", "com.huangyunkun.acpsure.cli"})
public class SureStepCliApplication implements CommandLineRunner, ExitCodeGenerator {

    private final SureStepCommand command;
    private final IFactory factory;
    private int exitCode;

    public SureStepCliApplication(SureStepCommand command, IFactory factory) {
        this.command = command;
        this.factory = factory;
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(command, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SureStepCliApplication.class, args)));
    }
}
