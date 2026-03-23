package com.huangyunkun.acpsure.core;

import com.google.common.io.Resources;
import com.huangyunkun.acpsure.cli.SureStepCliApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SureStepCliApplication.class)
@DirtiesContext
class BashExecIT {

    @Autowired
    private SureStep sureStep;

    @Test
    void shouldExecuteBashCommand() throws Exception {
        String configFile = Resources.getResource("simple-shell/config.xml").getFile();
        String taskFile = Resources.getResource("simple-shell/task.json").getFile();

        sureStep.intWithTaskAndFlowConfigFile(taskFile, configFile);
        RunningContainer container = sureStep.drive();

        Path outputFile = Path.of(container.getCodeWorkSpace()).resolve("output.txt");
        assertTrue(Files.exists(outputFile));
        assertThat(Files.readString(outputFile).trim(), is("hello"));
    }
}
