package com.huangyunkun;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.huangyunkun.cli.SureStepCliApplication;

@SpringBootTest(classes = SureStepCliApplication.class)
class SureStepIT {

    @Autowired
    private SureStep sureStep;

    @Test
    void shouldEnableRunAcp() throws Exception {
        String configFile = Resources.getResource("simple-acp-init/config.xml").getFile();
        String taskFile = Resources.getResource("simple-acp-init/task.json").getFile();

        sureStep.intWithTaskAndFlowConfigFile(taskFile, configFile);
        sureStep.drive();
    }
}