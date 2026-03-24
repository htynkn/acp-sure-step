package com.huangyunkun.acpsure.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeLogStoreTest {

    private NodeLogStore store;

    @BeforeEach
    void setUp() {
        store = new NodeLogStore();
    }

    @Test
    void shouldReturnEmptyListForUnknownNode() {
        List<String> logs = store.getLogs("unknown");
        assertTrue(logs.isEmpty());
    }

    @Test
    void shouldAddAndRetrieveLogs() {
        store.clearLogs("step1");
        store.addLog("step1", "first line");
        store.addLog("step1", "second line");

        List<String> logs = store.getLogs("step1");
        assertEquals(2, logs.size());
        assertEquals("first line", logs.get(0));
        assertEquals("second line", logs.get(1));
    }

    @Test
    void shouldClearLogsForNode() {
        store.addLog("step1", "old log");
        store.clearLogs("step1");

        List<String> logs = store.getLogs("step1");
        assertTrue(logs.isEmpty());
    }

    @Test
    void shouldClearAllLogs() {
        store.addLog("step1", "log1");
        store.addLog("step2", "log2");
        store.clearAll();

        assertTrue(store.getLogs("step1").isEmpty());
        assertTrue(store.getLogs("step2").isEmpty());
    }

    @Test
    void shouldKeepLogsIsolatedPerNode() {
        store.addLog("step1", "for step1");
        store.addLog("step2", "for step2");

        assertEquals(List.of("for step1"), store.getLogs("step1"));
        assertEquals(List.of("for step2"), store.getLogs("step2"));
    }
}
