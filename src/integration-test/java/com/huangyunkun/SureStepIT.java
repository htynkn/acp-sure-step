package com.huangyunkun;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SureStepIT {
    @Test
    void shouldEnableRunAcp() throws Exception {
        SureStep sureStep = new SureStep();
        String configFile = Resources.getResource("simple-acp-init/config.xml").getFile();
        String taskFile = Resources.getResource("simple-acp-init/task.json").getFile();

        sureStep.intWithTaskAndFlowConfigFile(taskFile, configFile);
        sureStep.drive();
    }
}