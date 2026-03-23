package com.huangyunkun.node;

import com.huangyunkun.acp.AcpService;
import com.huangyunkun.config.ConfigService;
import com.huangyunkun.config.dto.AcpInitTaskConfig;
import com.huangyunkun.config.dto.BaseTaskConfig;
import com.huangyunkun.util.ApplicationAwareUtil;
import com.yomahub.liteflow.core.NodeComponent;

import java.util.List;

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
