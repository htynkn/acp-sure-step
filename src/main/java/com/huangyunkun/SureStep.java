package com.huangyunkun;

import com.huangyunkun.config.ConfigService;
import com.huangyunkun.util.ApplicationAwareUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;

public class SureStep {
    private ConfigService configService;
    private FlowExecutor flowExecutor;

    SureStep() {
        this.configService = new ConfigService();
        ApplicationAwareUtil.regBean(configService);
    }

    public static SureStep newInstance() {
        return new SureStep();
    }

    public void intWithTaskAndFlowConfigFile(String taskFilePath, String flowFilePath) {
        configService.loadTask(taskFilePath);

        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource(flowFilePath);
        this.flowExecutor = FlowExecutorHolder.loadInstance(config);
        ApplicationAwareUtil.regBean(flowExecutor);
    }

    public RunningContainer drive() {
        LiteflowResponse response = flowExecutor.execute2Resp("defaultChain", "", RunningContainer.class);

        return response.getContextBean(RunningContainer.class);
    }
}
