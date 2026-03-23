package com.huangyunkun.cli;

import com.huangyunkun.SureStep;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Component
@Command(name = "sure-step", mixinStandardHelpOptions = true,
        description = "ACP Sure Step CLI - Execute task flows with ACP agents")
public class SureStepCommand implements Callable<Integer> {

    @Option(names = {"-t", "--task"}, description = "Path to the task configuration file (task.json)", required = true)
    private String taskFile;

    @Option(names = {"-f", "--flow"}, description = "Path to the flow configuration file (config.xml)", required = true)
    private String flowFile;

    private final SureStep sureStep;

    public SureStepCommand(SureStep sureStep) {
        this.sureStep = sureStep;
    }

    @Override
    public Integer call() throws Exception {
        sureStep.intWithTaskAndFlowConfigFile(taskFile, flowFile);
        sureStep.drive();
        return 0;
    }
}
