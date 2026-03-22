package com.huangyunkun.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huangyunkun.config.dto.BaseTaskConfig;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<BaseTaskConfig> loadTask(String taskConfigFilePath) {
        try {
            Map<String, Object> configMap = objectMapper.readValue(new File(taskConfigFilePath), new TypeReference<Map<String, Object>>() {});
            Object tasksObj = configMap.get("tasks");
            if (tasksObj == null) {
                return List.of();
            }
            return objectMapper.convertValue(tasksObj, new TypeReference<List<BaseTaskConfig>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load task config from: " + taskConfigFilePath, e);
        }
    }
}
