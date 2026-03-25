package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.RunningContainer;
import com.huangyunkun.acpsure.core.config.dto.VariableSetTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class VariableSetNode extends BaseNodeComponent {
    private static final Logger log = LoggerFactory.getLogger(VariableSetNode.class);

    @Override
    public void process() throws Exception {
        VariableSetTaskConfig taskConfig = (VariableSetTaskConfig) this.getCurrentTaskConfig();
        RunningContainer runningContainer = this.getContextBean(RunningContainer.class);

        Map<String, String> variables = taskConfig.getVariables();
        if (variables != null) {
            runningContainer.getVariables().putAll(variables);
            log.info("Set variables: {}", variables.keySet());
        }
    }
}
