package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.RunningContainer;
import com.huangyunkun.acpsure.core.config.dto.AcpInitTaskConfig;
import com.huangyunkun.acpsure.core.acp.AcpService;
import com.huangyunkun.acpsure.core.util.ApplicationAwareUtil;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AcpInitNode extends BaseNodeComponent {
    @Override
    public void process() throws Exception {
        AcpInitTaskConfig taskConfig = (AcpInitTaskConfig) this.getCurrentTaskConfig();
        AcpService acpService = new AcpService();

        ApplicationAwareUtil.regBean(acpService);

        Path workspacePath;
        if (StringUtils.isNotBlank(taskConfig.getWorkspace())) {
            workspacePath = Paths.get(taskConfig.getWorkspace());
            Files.createDirectories(workspacePath);
        } else {
            workspacePath = Files.createTempDirectory("acp-sure-step-");
        }

        RunningContainer runningContainer = this.getContextBean(RunningContainer.class);
        runningContainer.setCodeWorkSpace(workspacePath.toString());

        acpService.init(taskConfig.getCommand(), taskConfig.getArgs(), taskConfig.getEnv(), workspacePath.toString());
    }
}
