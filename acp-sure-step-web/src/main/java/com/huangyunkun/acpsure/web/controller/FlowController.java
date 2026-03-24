package com.huangyunkun.acpsure.web.controller;

import com.huangyunkun.acpsure.web.model.FlowStatus;
import com.huangyunkun.acpsure.web.service.FlowExecutionService;
import com.huangyunkun.acpsure.web.service.NodeLogStore;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Web controller exposing the flow visualization UI and REST API endpoints.
 */
@Controller
public class FlowController {

    private final FlowExecutionService flowExecutionService;
    private final NodeLogStore nodeLogStore;

    public FlowController(FlowExecutionService flowExecutionService, NodeLogStore nodeLogStore) {
        this.flowExecutionService = flowExecutionService;
        this.nodeLogStore = nodeLogStore;
    }

    /** Renders the main web UI page. */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Load flow configuration from the given task and flow files.
     * Returns the initial {@link FlowStatus} with the discovered steps.
     */
    @PostMapping("/api/flow/load")
    @ResponseBody
    public ResponseEntity<FlowStatus> loadFlow(
            @RequestParam("taskFile") String taskFile,
            @RequestParam("flowFile") String flowFile) {
        try {
            FlowStatus status = flowExecutionService.loadFlow(taskFile, flowFile);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            FlowStatus error = new FlowStatus();
            error.setLoaded(false);
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Start executing the loaded flow asynchronously.
     */
    @PostMapping("/api/flow/start")
    @ResponseBody
    public ResponseEntity<Map<String, String>> startFlow() {
        try {
            flowExecutionService.startFlow();
            return ResponseEntity.ok(Map.of("status", "started"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Poll the current execution status (step states).
     */
    @GetMapping("/api/flow/status")
    @ResponseBody
    public FlowStatus getStatus() {
        return flowExecutionService.getStatus();
    }

    /**
     * Return the captured log lines for the given step.
     */
    @GetMapping("/api/flow/logs/{stepId}")
    @ResponseBody
    public List<String> getLogs(@PathVariable("stepId") String stepId) {
        return nodeLogStore.getLogs(stepId);
    }
}
