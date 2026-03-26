package com.huangyunkun.acpsure.core.node;

import com.huangyunkun.acpsure.core.RunningContainer;
import com.huangyunkun.acpsure.core.config.dto.RepoWorkspaceInitTaskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RepoWorkspaceInitNode extends BaseNodeComponent {
    private static final Logger log = LoggerFactory.getLogger(RepoWorkspaceInitNode.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public void process() throws Exception {
        RepoWorkspaceInitTaskConfig taskConfig = (RepoWorkspaceInitTaskConfig) this.getCurrentTaskConfig();
        RunningContainer runningContainer = this.getContextBean(RunningContainer.class);

        String workspace = taskConfig.getWorkspace();
        String currentTime = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        Path repoWorkspacePath = Paths.get(workspace, "repos", currentTime);
        Files.createDirectories(repoWorkspacePath);

        log.info("Created repo workspace: {}", repoWorkspacePath);

        List<String> repos = taskConfig.getRepos();
        if (repos != null) {
            for (String repo : repos) {
                log.info("Cloning repository: {}", repo);
                ProcessResult result = new ProcessExecutor()
                        .command("git", "clone", repo)
                        .directory(repoWorkspacePath.toFile())
                        .readOutput(true)
                        .execute();

                String output = result.outputUTF8();
                if (!output.isEmpty()) {
                    log.info(output);
                }

                int exitCode = result.getExitValue();
                if (exitCode != 0) {
                    throw new RuntimeException("git clone failed with exit code " + exitCode + " for repo: " + repo);
                }
            }
        }

        runningContainer.getVariables().put("repoWorkSpace", repoWorkspacePath.toString());
        log.info("Set variable repoWorkSpace={}", repoWorkspacePath);
    }
}
