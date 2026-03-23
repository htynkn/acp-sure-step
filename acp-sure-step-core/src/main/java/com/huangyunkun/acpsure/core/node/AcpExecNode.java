package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.config.ConfigService;
import com.huangyunkun.acpsure.core.config.dto.AcpExecTaskConfig;
import com.huangyunkun.acpsure.core.util.ApplicationAwareUtil;
import com.huangyunkun.acpsure.core.acp.AcpService;

import java.nio.file.Files;
import java.nio.file.Path;

public class AcpExecNode extends BaseNodeComponent {
    @Override
    public void process() throws Exception {
        AcpExecTaskConfig taskConfig = (AcpExecTaskConfig) this.getCurrentTaskConfig();
        ConfigService configService = ApplicationAwareUtil.getBean(ConfigService.class);
        Path promptPath = configService.getTaskBaseDir().resolve(taskConfig.getPrompt());
        String promptText = Files.readString(promptPath);
        AcpService acpService = ApplicationAwareUtil.getBean(AcpService.class);
        acpService.exec(promptText);
    }
}
