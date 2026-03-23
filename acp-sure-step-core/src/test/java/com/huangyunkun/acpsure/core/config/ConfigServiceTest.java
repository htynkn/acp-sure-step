package com.huangyunkun.acpsure.core.config;

import com.google.common.io.Resources;
import com.huangyunkun.acpsure.core.config.dto.AcpExecTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.AcpInitTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.BaseTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.BashExecTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.TaskEnum;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

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
        assertThat(acpInitTaskConfig.getType(), CoreMatchers.is(TaskEnum.acpInit));

        BaseTaskConfig taskConfig2 = taskConfigs.get(1);
        assertThat(taskConfig2, instanceOf(AcpExecTaskConfig.class));
        AcpExecTaskConfig acpExecTaskConfig = (AcpExecTaskConfig) taskConfig2;
        assertThat(acpExecTaskConfig.getId(), is("cal"));
    }

    @Test
    void shouldEnableLoadSimpleShellTask() throws Exception {
        ConfigService configService = new ConfigService();

        List<BaseTaskConfig> taskConfigs = configService.loadTask(Resources.getResource("config/simple-shell/task.json").getFile());

        assertThat(taskConfigs, hasSize(3));

        BaseTaskConfig taskConfig1 = taskConfigs.get(0);
        assertThat(taskConfig1, instanceOf(AcpInitTaskConfig.class));
        assertThat(taskConfig1.getType(), CoreMatchers.is(TaskEnum.acpInit));

        BaseTaskConfig taskConfig2 = taskConfigs.get(1);
        assertThat(taskConfig2, instanceOf(BashExecTaskConfig.class));
        BashExecTaskConfig bashExecTaskConfig = (BashExecTaskConfig) taskConfig2;
        assertThat(bashExecTaskConfig.getId(), is("getIp"));
        assertThat(bashExecTaskConfig.getType(), CoreMatchers.is(TaskEnum.bashExec));
        assertThat(bashExecTaskConfig.getBash(), is("wget https://ipinfo.io/ip -qO- >> ip.txt"));

        BaseTaskConfig taskConfig3 = taskConfigs.get(2);
        assertThat(taskConfig3, instanceOf(AcpExecTaskConfig.class));
        assertThat(taskConfig3.getId(), is("summary"));
    }
}