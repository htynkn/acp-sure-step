package com.huangyunkun.acpsure.web.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store for per-step log messages.
 */
@Service
public class NodeLogStore {

    private final Map<String, List<String>> logs = new ConcurrentHashMap<>();

    public void clearLogs(String nodeId) {
        logs.put(nodeId, new ArrayList<>());
    }

    public void addLog(String nodeId, String message) {
        logs.computeIfAbsent(nodeId, k -> new ArrayList<>()).add(message);
    }

    public List<String> getLogs(String nodeId) {
        return Collections.unmodifiableList(logs.getOrDefault(nodeId, List.of()));
    }

    public void clearAll() {
        logs.clear();
    }
}
