package com.huangyunkun.acpsure.core.config;

import com.google.common.io.Resources;
import com.huangyunkun.acpsure.core.config.dto.AcpExecTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.AcpInitTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.BaseTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.BashExecTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.BashExecConditionTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.TaskEnum;
import com.huangyunkun.acpsure.core.config.dto.VariableSetTaskConfig;
import com.huangyunkun.acpsure.core.config.dto.RepoWorkspaceInitTaskConfig;
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

    @Test
    void shouldEnableLoadBashExecConditionTask() throws Exception {
        ConfigService configService = new ConfigService();

        List<BaseTaskConfig> taskConfigs = configService.loadTask(Resources.getResource("config/bash-exec-condition/task.json").getFile());

        assertThat(taskConfigs, hasSize(3));

        BaseTaskConfig taskConfig1 = taskConfigs.get(0);
        assertThat(taskConfig1, instanceOf(BashExecConditionTaskConfig.class));
        BashExecConditionTaskConfig bashExecConditionTaskConfig = (BashExecConditionTaskConfig) taskConfig1;
        assertThat(bashExecConditionTaskConfig.getId(), is("checkCondition"));
        assertThat(bashExecConditionTaskConfig.getType(), CoreMatchers.is(TaskEnum.bashExecCondition));
        assertThat(bashExecConditionTaskConfig.getBash(), is("echo yes"));
        assertThat(bashExecConditionTaskConfig.getExpectedResult(), is("yes"));

        BaseTaskConfig taskConfig2 = taskConfigs.get(1);
        assertThat(taskConfig2, instanceOf(BashExecTaskConfig.class));
        assertThat(taskConfig2.getId(), is("onTrue"));

        BaseTaskConfig taskConfig3 = taskConfigs.get(2);
        assertThat(taskConfig3, instanceOf(BashExecTaskConfig.class));
        assertThat(taskConfig3.getId(), is("onFalse"));
    }

    @Test
    void shouldEnableLoadVariableSetTask() throws Exception {
        ConfigService configService = new ConfigService();

        List<BaseTaskConfig> taskConfigs = configService.loadTask(Resources.getResource("config/variable-set/task.json").getFile());

        assertThat(taskConfigs, hasSize(2));

        BaseTaskConfig taskConfig1 = taskConfigs.get(0);
        assertThat(taskConfig1, instanceOf(VariableSetTaskConfig.class));
        VariableSetTaskConfig variableSetTaskConfig = (VariableSetTaskConfig) taskConfig1;
        assertThat(variableSetTaskConfig.getId(), is("setVars"));
        assertThat(variableSetTaskConfig.getType(), CoreMatchers.is(TaskEnum.variableSet));
        assertThat(variableSetTaskConfig.getVariables().get("TARGET"), is("world"));
        assertThat(variableSetTaskConfig.getVariables().get("OUTPUT_FILE"), is("output.txt"));

        BaseTaskConfig taskConfig2 = taskConfigs.get(1);
        assertThat(taskConfig2, instanceOf(BashExecTaskConfig.class));
        assertThat(taskConfig2.getId(), is("printHello"));
    }

    @Test
    void shouldEnableLoadRepoWorkspaceInitTask() throws Exception {
        ConfigService configService = new ConfigService();

        List<BaseTaskConfig> taskConfigs = configService.loadTask(Resources.getResource("config/repo-workspace-init/task.json").getFile());

        assertThat(taskConfigs, hasSize(2));

        BaseTaskConfig taskConfig1 = taskConfigs.get(0);
        assertThat(taskConfig1, instanceOf(RepoWorkspaceInitTaskConfig.class));
        RepoWorkspaceInitTaskConfig repoWorkspaceInitTaskConfig = (RepoWorkspaceInitTaskConfig) taskConfig1;
        assertThat(repoWorkspaceInitTaskConfig.getId(), is("initRepos"));
        assertThat(repoWorkspaceInitTaskConfig.getType(), CoreMatchers.is(TaskEnum.repoWorkspaceInit));
        assertThat(repoWorkspaceInitTaskConfig.getWorkspace(), is("/tmp/test-workspace"));
        assertThat(repoWorkspaceInitTaskConfig.getRepos(), hasSize(2));

        BaseTaskConfig taskConfig2 = taskConfigs.get(1);
        assertThat(taskConfig2, instanceOf(BashExecTaskConfig.class));
        assertThat(taskConfig2.getId(), is("listFiles"));
    }
}