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

class BashExecNodeTest {

    @TempDir
    Path workDir;

    @Test
    void shouldExecuteBashCommandInWorkDir() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "echo hello > output.txt")
                .directory(workDir.toFile())
                .readOutput(true)
                .execute();

        assertThat(result.getExitValue(), is(0));

        Path outputFile = workDir.resolve("output.txt");
        assertTrue(Files.exists(outputFile));
        assertThat(Files.readString(outputFile).trim(), is("hello"));
    }

    @Test
    void shouldCaptureCommandOutput() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "echo hello world")
                .directory(workDir.toFile())
                .readOutput(true)
                .execute();

        assertThat(result.getExitValue(), is(0));
        assertThat(result.outputUTF8().trim(), is("hello world"));
    }

    @Test
    void shouldReturnNonZeroExitCodeOnFailure() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "exit 42")
                .directory(workDir.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        assertThat(result.getExitValue(), is(42));
    }

    @Test
    void shouldUseWorkDirAsCurrentDirectory() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "pwd")
                .directory(workDir.toFile())
                .readOutput(true)
                .execute();

        assertThat(result.getExitValue(), is(0));
        assertThat(result.outputUTF8().trim(), is(workDir.toRealPath().toString()));
    }
}
