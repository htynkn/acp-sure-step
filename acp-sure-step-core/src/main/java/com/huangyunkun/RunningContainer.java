package com.huangyunkun;

import com.huangyunkun.config.dto.BaseTaskConfig;
import lombok.Data;

import java.util.List;

@Data
public class RunningContainer {
    private String sessionId;
    private String agentWorkSpace;
    private String codeWorkSpace;
    private List<BaseTaskConfig> taskConfigs;
}
