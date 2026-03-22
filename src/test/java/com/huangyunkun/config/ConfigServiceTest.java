package com.huangyunkun.config;

import com.google.common.io.Resources;
import com.huangyunkun.config.dto.AcpExecTaskConfig;
import com.huangyunkun.config.dto.AcpInitTaskConfig;
import com.huangyunkun.config.dto.BaseTaskConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;


class ConfigServiceTest {
    @Test
    void shouldEnableLoadTask() throws Exception {
        ConfigService configService = new ConfigService();

        List<BaseTaskConfig> taskConfigs = configService.loadTask(Resources.getResource("config/simple-acp-init/task.json").getFile());

        assertThat(taskConfigs, hasSize(2));

        BaseTaskConfig taskConfig1 = taskConfigs.get(0);
        assertThat(taskConfig1, instanceOf(AcpInitTaskConfig.class));
        AcpInitTaskConfig acpInitTaskConfig = (AcpInitTaskConfig) taskConfig1;
        assertThat(acpInitTaskConfig.getCommand(), is("qwen"));
        assertThat(acpInitTaskConfig.getArgs(), hasSize(1));

        BaseTaskConfig taskConfig2 = taskConfigs.get(1);
        assertThat(taskConfig2, instanceOf(AcpExecTaskConfig.class));
        AcpExecTaskConfig acpExecTaskConfig = (AcpExecTaskConfig) taskConfig2;
        assertThat(acpExecTaskConfig.getId(), is("cal"));
    }
}