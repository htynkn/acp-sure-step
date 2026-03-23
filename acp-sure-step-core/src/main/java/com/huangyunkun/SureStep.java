package com.huangyunkun;

import com.huangyunkun.config.ConfigService;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SureStep {
    private final ConfigService configService;
    private FlowExecutor flowExecutor;

    public SureStep(ConfigService configService) {
        this.configService = configService;
    }

    public void intWithTaskAndFlowConfigFile(String taskFilePath, String flowFilePath) throws IOException {
        configService.loadTask(taskFilePath);

        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource(flowFilePath);
        this.flowExecutor = FlowExecutorHolder.loadInstance(config);
    }

    public RunningContainer drive() {
        LiteflowResponse response = flowExecutor.execute2Resp("defaultChain", "", RunningContainer.class);

        return response.getContextBean(RunningContainer.class);
    }
}
