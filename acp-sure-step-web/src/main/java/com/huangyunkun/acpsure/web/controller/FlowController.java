package com.huangyunkun.acpsure.web.controller;

import com.huangyunkun.acpsure.web.model.FileEntry;
import com.huangyunkun.acpsure.web.model.FileListing;
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

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
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

    /**
     * List files and directories at the given server-side path.
     * Defaults to the user home directory when no path is supplied.
     */
    @GetMapping("/api/files")
    @ResponseBody
    public ResponseEntity<?> listFiles(
            @RequestParam(value = "path", defaultValue = "") String path) {
        String startPath = path.isBlank() ? System.getProperty("user.home") : path;
        File dir = new File(startPath).getAbsoluteFile();
        if (!dir.exists()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Path does not exist: " + dir));
        }
        if (!dir.isDirectory()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not a directory: " + dir));
        }
        File[] children = dir.listFiles();
        List<FileEntry> entries = (children == null ? new File[0] : children).length == 0
                ? List.of()
                : Arrays.stream(children == null ? new File[0] : children)
                        .filter(f -> !f.isHidden())
                        .sorted(Comparator.comparing(File::isFile)   // directories first
                                          .thenComparing(f -> f.getName().toLowerCase()))
                        .map(f -> new FileEntry(f.getName(), f.getAbsolutePath(), f.isDirectory()))
                        .toList();

        File parent = dir.getParentFile();
        String parentPath = parent != null ? parent.getAbsolutePath() : null;
        return ResponseEntity.ok(new FileListing(dir.getAbsolutePath(), parentPath, entries));
    }
}
