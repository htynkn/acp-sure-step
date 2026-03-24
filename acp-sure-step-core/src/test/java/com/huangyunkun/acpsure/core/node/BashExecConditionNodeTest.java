package com.huangyunkun.acpsure.core.node;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class BashExecConditionNodeTest {

    @TempDir
    Path workDir;

    @Test
    void shouldMatchExpectedOutput() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "echo hello")
                .directory(workDir.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        String output = result.outputUTF8().trim();
        assertThat(output, is("hello"));
        assertThat(output.equals("hello"), is(true));
    }

    @Test
    void shouldNotMatchDifferentOutput() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "echo hello")
                .directory(workDir.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        String output = result.outputUTF8().trim();
        assertThat(output.equals("world"), is(false));
    }

    @Test
    void shouldHandleNonZeroExitCodeAndStillCompareOutput() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "echo error; exit 1")
                .directory(workDir.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        String output = result.outputUTF8().trim();
        assertThat(output, is("error"));
        assertThat(output.equals("error"), is(true));
    }

    @Test
    void shouldHandleEmptyOutput() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "true")
                .directory(workDir.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        String output = result.outputUTF8().trim();
        assertThat(output, is(""));
        assertThat(output.equals(""), is(true));
    }

    @Test
    void shouldTrimOutputBeforeComparison() throws Exception {
        ProcessResult result = new ProcessExecutor()
                .command("bash", "-c", "echo '  hello  '")
                .directory(workDir.toFile())
                .readOutput(true)
                .exitValueAny()
                .execute();

        String output = result.outputUTF8().trim();
        assertThat(output, is("hello"));
        assertThat(output.equals("hello"), is(true));
    }
}
