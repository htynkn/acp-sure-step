package com.huangyunkun.acpsure.core;

import com.huangyunkun.acpsure.core.config.dto.BaseTaskConfig;
import lombok.Data;

import java.util.List;

@Data
public class RunningContainer {
    private String sessionId;
    private String agentWorkSpace;
    private String codeWorkSpace;
    private List<BaseTaskConfig> taskConfigs;
}
