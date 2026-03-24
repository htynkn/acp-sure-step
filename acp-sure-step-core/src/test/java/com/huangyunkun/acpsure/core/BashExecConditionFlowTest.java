package com.huangyunkun.acpsure.core;

import com.huangyunkun.acpsure.core.config.ConfigService;
import com.huangyunkun.acpsure.core.util.ApplicationAwareUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BashExecConditionFlowTest {

    @TempDir
    Path tempDir;

    private ConfigService configService;
    private SureStep sureStep;

    @BeforeEach
    void setUp() {
        configService = new ConfigService();
        ApplicationAwareUtil.regBean(configService, ConfigService.class);
        sureStep = new SureStep(configService);
    }

    @AfterEach
    void tearDown() {
        ApplicationAwareUtil.clear();
    }

    @Test
    void shouldExecuteTrueBranchWhenConditionMatches() throws Exception {
        Path resultFile = tempDir.resolve("result.txt");

        String taskJson = """
                {
                  "tasks": [
                    {
                      "id": "checkCondition",
                      "type": "bashExecCondition",
                      "bash": "echo yes",
                      "expectedResult": "yes"
                    },
                    {
                      "id": "onTrue",
                      "type": "bashExec",
                      "bash": "echo true_branch > %s"
                    },
                    {
                      "id": "onFalse",
                      "type": "bashExec",
                      "bash": "echo false_branch > %s"
                    }
                  ]
                }
                """.formatted(resultFile.toAbsolutePath(), resultFile.toAbsolutePath());

        String configXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <flow>
                    <chain name="defaultChain">
                        IF(checkCondition, onTrue, onFalse);
                    </chain>
                </flow>
                """;

        Path taskFile = tempDir.resolve("task.json");
        Path configFile = tempDir.resolve("config.xml");
        Files.writeString(taskFile, taskJson);
        Files.writeString(configFile, configXml);

        sureStep.intWithTaskAndFlowConfigFile(taskFile.toString(), configFile.toString());
        sureStep.drive();

        assertTrue(Files.exists(resultFile));
        assertThat(Files.readString(resultFile).trim(), is("true_branch"));
    }

    @Test
    void shouldExecuteFalseBranchWhenConditionDoesNotMatch() throws Exception {
        Path resultFile = tempDir.resolve("result.txt");

        String taskJson = """
                {
                  "tasks": [
                    {
                      "id": "checkCondition",
                      "type": "bashExecCondition",
                      "bash": "echo no",
                      "expectedResult": "yes"
                    },
                    {
                      "id": "onTrue",
                      "type": "bashExec",
                      "bash": "echo true_branch > %s"
                    },
                    {
                      "id": "onFalse",
                      "type": "bashExec",
                      "bash": "echo false_branch > %s"
                    }
                  ]
                }
                """.formatted(resultFile.toAbsolutePath(), resultFile.toAbsolutePath());

        String configXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <flow>
                    <chain name="defaultChain">
                        IF(checkCondition, onTrue, onFalse);
                    </chain>
                </flow>
                """;

        Path taskFile = tempDir.resolve("task.json");
        Path configFile = tempDir.resolve("config.xml");
        Files.writeString(taskFile, taskJson);
        Files.writeString(configFile, configXml);

        sureStep.intWithTaskAndFlowConfigFile(taskFile.toString(), configFile.toString());
        sureStep.drive();

        assertTrue(Files.exists(resultFile));
        assertThat(Files.readString(resultFile).trim(), is("false_branch"));
    }
}

