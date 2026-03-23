package com.huangyunkun.node;

import com.google.common.base.Preconditions;
import com.huangyunkun.config.ConfigService;
import com.huangyunkun.config.dto.BaseTaskConfig;
import com.huangyunkun.util.ApplicationAwareUtil;
import com.yomahub.liteflow.core.NodeComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public abstract class BaseNodeComponent extends NodeComponent {
    BaseTaskConfig getCurrentTaskConfig() {
        ConfigService configService = ApplicationAwareUtil.getBean(ConfigService.class);
        List<BaseTaskConfig> taskConfigs = configService.getTaskConfigs();
        String nodeId = this.getNodeId();
        Optional<BaseTaskConfig> first = taskConfigs.stream().filter(p -> StringUtils.equals(nodeId, p.getId())).findFirst();
        Preconditions.checkArgument(first.isPresent(), "Can't find task config for current node");
        return first.get();
    }
}
