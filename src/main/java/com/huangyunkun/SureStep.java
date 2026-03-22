package com.huangyunkun;

import com.huangyunkun.config.ConfigService;

public class SureStep {
    public static SureStep newInstance() {
        return new SureStep();
    }

    public void intWithTaskAndFlowConfigFile(String taskFilePath, String flowFilePath) {
        ConfigService configService = new ConfigService();
        configService.loadTask(taskFilePath);
    }
}
