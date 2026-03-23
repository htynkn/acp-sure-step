package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.RunningContainer;
import com.huangyunkun.acpsure.core.config.dto.AcpInitTaskConfig;
import com.huangyunkun.acpsure.core.acp.AcpService;
import com.huangyunkun.acpsure.core.util.ApplicationAwareUtil;

import java.nio.file.Files;
import java.nio.file.Path;

public class AcpInitNode extends BaseNodeComponent {
    @Override
    public void process() throws Exception {
        AcpInitTaskConfig taskConfig = (AcpInitTaskConfig) this.getCurrentTaskConfig();
        AcpService acpService = new AcpService();

        ApplicationAwareUtil.regBean(acpService);

        Path tmp = Files.createTempDirectory("acp-sure-step-");

        RunningContainer runningContainer = this.getContextBean(RunningContainer.class);
        runningContainer.setCodeWorkSpace(tmp.toString());

        acpService.init(taskConfig.getCommand(), taskConfig.getArgs(), taskConfig.getEnv(), tmp.toString());
    }
}
