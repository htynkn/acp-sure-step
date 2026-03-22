package com.huangyunkun.config;

import com.google.common.io.Resources;
import com.huangyunkun.config.dto.AcpInitTaskConfig;
import com.huangyunkun.config.dto.BaseTaskConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;


class ConfigServiceTest {
    @Test
    void shouldEnableLoadTask() {
        ConfigService configService = new ConfigService();

        List<BaseTaskConfig> taskConfigs = configService.loadTask(Resources.getResource("config/simple-acp-init/task.json").getFile());

        assertThat(taskConfigs, hasSize(1));
        BaseTaskConfig taskConfig = taskConfigs.get(0);
        assertThat(taskConfig, instanceOf(AcpInitTaskConfig.class));
        AcpInitTaskConfig acpInitTaskConfig = (AcpInitTaskConfig) taskConfig;
        assertThat(acpInitTaskConfig.getCommand(), is("qwen"));
        assertThat(acpInitTaskConfig.getArgs(), hasSize(1));
    }
}