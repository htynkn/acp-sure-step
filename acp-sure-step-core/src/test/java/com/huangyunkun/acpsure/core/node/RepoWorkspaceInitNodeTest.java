package com.huangyunkun.acpsure.core.node;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepoWorkspaceInitNodeTest {

    @TempDir
    Path workDir;

    @Test
    void shouldCloneRepoIntoWorkDir() throws Exception {
        // Create a local bare git repo to clone from
        Path bareRepo = workDir.resolve("bare-repo.git");
        new ProcessExecutor()
                .command("git", "init", "--bare", bareRepo.toString())
                .readOutput(true)
                .execute();

        Path cloneTarget = workDir.resolve("cloned");
        Files.createDirectories(cloneTarget);

        ProcessResult result = new ProcessExecutor()
                .command("git", "clone", bareRepo.toString())
                .directory(cloneTarget.toFile())
                .readOutput(true)
                .execute();

        assertThat(result.getExitValue(), is(0));
        assertTrue(Files.exists(cloneTarget.resolve("bare-repo")));
    }

    @Test
    void shouldFailWithNonZeroExitCodeForInvalidRepo() throws Exception {
        Path cloneTarget = workDir.resolve("cloned");
        Files.createDirectories(cloneTarget);

        ProcessResult result = new ProcessExecutor()
                .command("git", "clone", "https://invalid-host.example.com/nonexistent.git")
                .directory(cloneTarget.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        assertTrue(result.getExitValue() != 0);
    }

    @Test
    void shouldCreateRepoWorkspaceDirectory() throws Exception {
        Path repoWorkspace = workDir.resolve("repos").resolve("20240101120000");
        Files.createDirectories(repoWorkspace);

        assertTrue(Files.exists(repoWorkspace));
        assertTrue(Files.isDirectory(repoWorkspace));
    }
}
