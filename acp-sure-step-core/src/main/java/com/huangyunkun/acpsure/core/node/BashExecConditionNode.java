package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.RunningContainer;
import com.huangyunkun.acpsure.core.config.dto.BashExecConditionTaskConfig;
import com.huangyunkun.acpsure.core.util.VariableSubstitutionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.File;

public class BashExecConditionNode extends BaseBooleanNodeComponent {
    private static final Logger log = LoggerFactory.getLogger(BashExecConditionNode.class);

    @Override
    public boolean processBoolean() throws Exception {
        BashExecConditionTaskConfig taskConfig = (BashExecConditionTaskConfig) this.getCurrentTaskConfig();
        RunningContainer runningContainer = this.getContextBean(RunningContainer.class);

        String workDir = runningContainer.getCodeWorkSpace();
        String bash = VariableSubstitutionUtil.substitute(taskConfig.getBash(), runningContainer.getVariables());

        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", bash)
                .directory(workDir != null ? new File(workDir) : null)
                .readOutput(true)
                .exitValueAny()
                .execute();

        String output = result.outputUTF8().trim();
        if (!output.isEmpty()) {
            log.info(output);
        }

        String expectedResult = taskConfig.getExpectedResult();
        boolean matched = output.equals(expectedResult);
        log.info("Bash condition result: output='{}', expected='{}', matched={}", output, expectedResult, matched);
        return matched;
    }
}

