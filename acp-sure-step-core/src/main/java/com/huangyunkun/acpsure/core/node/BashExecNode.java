package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.RunningContainer;
import com.huangyunkun.acpsure.core.config.dto.BashExecTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.File;

public class BashExecNode extends BaseNodeComponent {
    private static final Logger log = LoggerFactory.getLogger(BashExecNode.class);

    @Override
    public void process() throws Exception {
        BashExecTaskConfig taskConfig = (BashExecTaskConfig) this.getCurrentTaskConfig();
        RunningContainer runningContainer = this.getContextBean(RunningContainer.class);

        String workDir = runningContainer.getCodeWorkSpace();

        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", taskConfig.getBash())
                .directory(workDir != null ? new File(workDir) : null)
                .readOutput(true)
                .execute();

        String output = result.outputUTF8();
        if (!output.isEmpty()) {
            log.info(output);
        }

        int exitCode = result.getExitValue();
        if (exitCode != 0) {
            throw new RuntimeException("Bash command exited with code " + exitCode + ": " + taskConfig.getBash());
        }
    }
}
