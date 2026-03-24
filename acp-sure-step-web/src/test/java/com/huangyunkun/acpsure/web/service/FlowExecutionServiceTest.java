package com.huangyunkun.acpsure.web.service;

import com.huangyunkun.acpsure.core.SureStep;
import com.huangyunkun.acpsure.web.model.FlowStatus;
import com.huangyunkun.acpsure.web.model.StepState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlowExecutionServiceTest {

    @TempDir
    Path tempDir;

    private SureStep sureStep;
    private NodeLogStore nodeLogStore;
    private FlowExecutionService service;

    @BeforeEach
    void setUp() {
        sureStep = mock(SureStep.class);
        nodeLogStore = new NodeLogStore();
        service = new FlowExecutionService(sureStep, nodeLogStore);
    }

    // ---- parseChainFromXml / extractNodeIds ----

    @Test
    void shouldExtractNodeIdsFromThenExpression() {
        List<String> ids = service.extractNodeIds("THEN(stepA, stepB, stepC);");
        assertEquals(List.of("stepA", "stepB", "stepC"), ids);
    }

    @Test
    void shouldSkipLiteFlowKeywords() {
        List<String> ids = service.extractNodeIds("IF(cond, thenStep, elseStep);");
        assertEquals(List.of("cond", "thenStep", "elseStep"), ids);
    }

    @Test
    void shouldParseXmlAndExtractSteps() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <flow>
                    <chain name="defaultChain">
                        THEN(alpha, beta, gamma);
                    </chain>
                </flow>
                """;
        Path flowFile = tempDir.resolve("config.xml");
        Files.writeString(flowFile, xml);

        List<String> ids = service.parseChainFromXml(flowFile.toString());
        assertEquals(List.of("alpha", "beta", "gamma"), ids);
    }

    @Test
    void shouldReturnEmptyListWhenNoChainInXml() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <flow></flow>
                """;
        Path flowFile = tempDir.resolve("config.xml");
        Files.writeString(flowFile, xml);

        List<String> ids = service.parseChainFromXml(flowFile.toString());
        assertTrue(ids.isEmpty());
    }

    // ---- loadFlow ----

    @Test
    void shouldLoadFlowAndPopulateSteps() throws Exception {
        String taskJson = """
                { "tasks": [] }
                """;
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <flow>
                    <chain name="defaultChain">
                        THEN(first, second);
                    </chain>
                </flow>
                """;
        Path taskFile = tempDir.resolve("task.json");
        Path flowFile = tempDir.resolve("config.xml");
        Files.writeString(taskFile, taskJson);
        Files.writeString(flowFile, xml);

        FlowStatus status = service.loadFlow(taskFile.toString(), flowFile.toString());

        assertTrue(status.isLoaded());
        assertFalse(status.isRunning());
        assertNotNull(status.getSteps());
        assertEquals(2, status.getSteps().size());
        assertEquals("first", status.getSteps().get(0).getId());
        assertEquals(StepState.PENDING, status.getSteps().get(0).getState());
    }

    // ---- nodeStarted / nodeCompleted / nodeFailed ----

    @Test
    void shouldUpdateStepStateOnNodeStarted() throws Exception {
        loadTwoStepFlow();
        service.nodeStarted("first");
        assertEquals(StepState.RUNNING, service.getStatus().getSteps().get(0).getState());
    }

    @Test
    void shouldUpdateStepStateOnNodeCompleted() throws Exception {
        loadTwoStepFlow();
        service.nodeStarted("first");
        service.nodeCompleted("first");
        assertEquals(StepState.COMPLETED, service.getStatus().getSteps().get(0).getState());
    }

    @Test
    void shouldUpdateStepStateOnNodeFailed() throws Exception {
        loadTwoStepFlow();
        service.nodeStarted("first");
        service.nodeFailed("first", "oops");
        assertEquals(StepState.FAILED, service.getStatus().getSteps().get(0).getState());
        assertEquals("oops", service.getStatus().getSteps().get(0).getErrorMessage());
    }

    @Test
    void shouldThrowWhenStartFlowCalledBeforeLoad() {
        assertThrows(IllegalStateException.class, () -> service.startFlow());
    }

    // ---- helpers ----

    private void loadTwoStepFlow() throws Exception {
        String taskJson = "{ \"tasks\": [] }";
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <flow>
                    <chain name="defaultChain">THEN(first, second);</chain>
                </flow>
                """;
        Path taskFile = tempDir.resolve("task.json");
        Path flowFile = tempDir.resolve("config.xml");
        Files.writeString(taskFile, taskJson);
        Files.writeString(flowFile, xml);
        service.loadFlow(taskFile.toString(), flowFile.toString());
    }
}
