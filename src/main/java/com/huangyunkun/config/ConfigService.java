package com.huangyunkun.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huangyunkun.config.dto.BaseTaskConfig;
import com.huangyunkun.config.dto.TaskEnum;
import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<BaseTaskConfig> taskConfigs;

    public List<BaseTaskConfig> loadTask(String taskConfigFilePath) throws IOException {
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
                        .setClazz("com.huangyunkun.node.AcpInitNode")
                        .build();
            } else if (taskConfig.getType() == TaskEnum.acpExec) {
                LiteFlowNodeBuilder.createCommonNode().setId(taskConfig.getId())
                        .setName(taskConfig.getId())
                        .setClazz("com.huangyunkun.node.AcpExecNode")
                        .build();
            }
        }

        return taskConfigs;
    }

    public List<BaseTaskConfig> getTaskConfigs() {
        return this.taskConfigs;
    }
}
