package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.config.dto.AcpInitTaskConfig;
import com.huangyunkun.acpsure.core.acp.AcpService;
import com.huangyunkun.acpsure.core.util.ApplicationAwareUtil;

public class AcpInitNode extends BaseNodeComponent {
    @Override
    public void process() throws Exception {
        AcpInitTaskConfig taskConfig = (AcpInitTaskConfig) this.getCurrentTaskConfig();
        AcpService acpService = new AcpService();

        ApplicationAwareUtil.regBean(acpService);

        String tmp = "/home/htynkn/Opensource/acp-sure-step";

        acpService.init(taskConfig.getCommand(), taskConfig.getArgs(), taskConfig.getEnv(), tmp);
    }
}
