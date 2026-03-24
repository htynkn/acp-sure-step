package com.huangyunkun.acpsure.core.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huangyunkun.acpsure.core.config.dto.BaseTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.TaskEnum;
import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Component
public class ConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<BaseTaskConfig> taskConfigs;
    private Path taskBaseDir;

    public Path getTaskBaseDir() {
        return taskBaseDir;
    }

    public List<BaseTaskConfig> loadTask(String taskConfigFilePath) throws IOException {
        this.taskBaseDir = new File(taskConfigFilePath).toPath().getParent();
        Map<String, Object> configMap = objectMapper.readValue(new File(taskConfigFilePath), new TypeReference<Map<String, Object>>() {
        });
        Object tasksObj = configMap.get("tasks");
        if (tasksObj == null) {
            return List.of();
        }
        List<BaseTaskConfig> taskConfigs = objectMapper.convertValue(tasksObj, new TypeReference<List<BaseTaskConfig>>() {
        });
        this.taskConfigs = taskConfigs;

        for (BaseTaskConfig taskConfig : taskConfigs) {
            if (taskConfig.getType() == TaskEnum.acpInit) {
                LiteFlowNodeBuilder.createCommonNode().setId(taskConfig.getId())
                        .setName(taskConfig.getId())
                        .setClazz("com.huangyunkun.acpsure.core.node.AcpInitNode")
                        .build();
            } else if (taskConfig.getType() == TaskEnum.acpExec) {
                LiteFlowNodeBuilder.createCommonNode().setId(taskConfig.getId())
                        .setName(taskConfig.getId())
                        .setClazz("com.huangyunkun.acpsure.core.node.AcpExecNode")
                        .build();
            } else if (taskConfig.getType() == TaskEnum.bashExec) {
                LiteFlowNodeBuilder.createCommonNode().setId(taskConfig.getId())
                        .setName(taskConfig.getId())
                        .setClazz("com.huangyunkun.acpsure.core.node.BashExecNode")
                        .build();
            } else if (taskConfig.getType() == TaskEnum.bashExecCondition) {
                LiteFlowNodeBuilder.createBooleanNode().setId(taskConfig.getId())
                        .setName(taskConfig.getId())
                        .setClazz("com.huangyunkun.acpsure.core.node.BashExecConditionNode")
                        .build();
            }
        }

        return taskConfigs;
    }

    public List<BaseTaskConfig> getTaskConfigs() {
        return this.taskConfigs;
    }
}
