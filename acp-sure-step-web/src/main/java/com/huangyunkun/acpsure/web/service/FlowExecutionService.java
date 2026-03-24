package com.huangyunkun.acpsure.web.service;

import com.huangyunkun.acpsure.core.SureStep;
import com.huangyunkun.acpsure.web.model.FlowStatus;
import com.huangyunkun.acpsure.web.model.StepInfo;
import com.huangyunkun.acpsure.web.model.StepState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages the lifecycle of a flow execution: loading, starting, tracking step states.
 */
@Service
public class FlowExecutionService {

    private static final Logger log = LoggerFactory.getLogger(FlowExecutionService.class);

    private static final Set<String> LITEFLOW_KEYWORDS = Set.of(
            "THEN", "IF", "ELSE", "WHEN", "NOT", "AND", "OR",
            "FOR", "WHILE", "DO", "BREAK", "SWITCH", "TO", "DEFAULT", "TRUE", "FALSE"
    );

    private final SureStep sureStep;
    private final NodeLogStore nodeLogStore;

    private final FlowStatus status = new FlowStatus();
    private final Map<String, StepInfo> stepMap = new ConcurrentHashMap<>();

    public FlowExecutionService(SureStep sureStep, NodeLogStore nodeLogStore) {
        this.sureStep = sureStep;
        this.nodeLogStore = nodeLogStore;
    }

    /**
     * Load flow configuration from the provided task JSON and flow XML files.
     * Parses the XML to discover the step chain structure.
     */
    public FlowStatus loadFlow(String taskFilePath, String flowFilePath) throws Exception {
        List<String> stepIds = parseChainFromXml(flowFilePath);

        nodeLogStore.clearAll();
        stepMap.clear();

        List<StepInfo> steps = new ArrayList<>();
        for (String id : stepIds) {
            StepInfo stepInfo = new StepInfo(id);
            steps.add(stepInfo);
            stepMap.put(id, stepInfo);
        }

        status.setLoaded(true);
        status.setRunning(false);
        status.setCompleted(false);
        status.setFailed(false);
        status.setErrorMessage(null);
        status.setSteps(steps);

        sureStep.intWithTaskAndFlowConfigFile(taskFilePath, flowFilePath);

        return status;
    }

    /**
     * Start executing the loaded flow asynchronously.
     */
    public void startFlow() {
        if (!status.isLoaded()) {
            throw new IllegalStateException("Flow not loaded. Call loadFlow() first.");
        }
        if (status.isRunning()) {
            throw new IllegalStateException("Flow is already running.");
        }

        status.setRunning(true);
        status.setCompleted(false);
        status.setFailed(false);
        status.setErrorMessage(null);

        // Reset all steps to pending
        if (status.getSteps() != null) {
            status.getSteps().forEach(s -> s.setState(StepState.PENDING));
        }

        CompletableFuture.runAsync(() -> {
            try {
                sureStep.drive();
                status.setRunning(false);
                status.setCompleted(true);
                // Mark any remaining PENDING or RUNNING steps as COMPLETED
                if (status.getSteps() != null) {
                    status.getSteps().stream()
                            .filter(s -> s.getState() == StepState.PENDING || s.getState() == StepState.RUNNING)
                            .forEach(s -> s.setState(StepState.COMPLETED));
                }
            } catch (Exception e) {
                log.error("Flow execution failed", e);
                status.setRunning(false);
                status.setFailed(true);
                status.setErrorMessage(e.getMessage());
                // Mark any RUNNING step as FAILED
                if (status.getSteps() != null) {
                    status.getSteps().stream()
                            .filter(s -> s.getState() == StepState.RUNNING)
                            .forEach(s -> {
                                s.setState(StepState.FAILED);
                                s.setErrorMessage(e.getMessage());
                            });
                }
            }
        });
    }

    /**
     * Get the current flow status snapshot.
     */
    public FlowStatus getStatus() {
        return status;
    }

    /**
     * Called by the aspect when a node begins processing.
     */
    public void nodeStarted(String nodeId) {
        nodeLogStore.clearLogs(nodeId);
        StepInfo stepInfo = stepMap.get(nodeId);
        if (stepInfo != null) {
            stepInfo.setState(StepState.RUNNING);
        }
    }

    /**
     * Called by the aspect when a node finishes successfully.
     */
    public void nodeCompleted(String nodeId) {
        StepInfo stepInfo = stepMap.get(nodeId);
        if (stepInfo != null) {
            stepInfo.setState(StepState.COMPLETED);
        }
    }

    /**
     * Called by the aspect when a node fails.
     */
    public void nodeFailed(String nodeId, String errorMessage) {
        StepInfo stepInfo = stepMap.get(nodeId);
        if (stepInfo != null) {
            stepInfo.setState(StepState.FAILED);
            stepInfo.setErrorMessage(errorMessage);
        }
    }

    /**
     * Parse the LiteFlow XML file and return step IDs in order from the defaultChain.
     */
    List<String> parseChainFromXml(String flowFilePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setExpandEntityReferences(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(flowFilePath));

        NodeList chains = doc.getElementsByTagName("chain");
        for (int i = 0; i < chains.getLength(); i++) {
            org.w3c.dom.Element chain = (org.w3c.dom.Element) chains.item(i);
            String expression = chain.getTextContent().trim();
            return extractNodeIds(expression);
        }
        return List.of();
    }

    /**
     * Extract node IDs from a LiteFlow EL expression, skipping LiteFlow keywords.
     */
    List<String> extractNodeIds(String expression) {
        List<String> nodeIds = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b([a-zA-Z][a-zA-Z0-9_]*)\\b");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String id = matcher.group(1);
            if (!LITEFLOW_KEYWORDS.contains(id)) {
                nodeIds.add(id);
            }
        }
        return nodeIds;
    }
}
