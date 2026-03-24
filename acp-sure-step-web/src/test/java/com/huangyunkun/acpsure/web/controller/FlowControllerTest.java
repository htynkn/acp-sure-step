package com.huangyunkun.acpsure.web.controller;

import com.huangyunkun.acpsure.web.model.FlowStatus;
import com.huangyunkun.acpsure.web.model.StepInfo;
import com.huangyunkun.acpsure.web.model.StepState;
import com.huangyunkun.acpsure.web.service.FlowExecutionService;
import com.huangyunkun.acpsure.web.service.NodeLogStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlowController.class)
class FlowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FlowExecutionService flowExecutionService;

    @MockitoBean
    NodeLogStore nodeLogStore;

    @Test
    void indexShouldReturnHtml() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void loadFlowShouldReturnStatus() throws Exception {
        FlowStatus status = new FlowStatus();
        status.setLoaded(true);
        StepInfo step = new StepInfo("step1");
        step.setState(StepState.PENDING);
        status.setSteps(List.of(step));

        when(flowExecutionService.loadFlow("/task.json", "/flow.xml")).thenReturn(status);

        mockMvc.perform(post("/api/flow/load")
                        .param("taskFile", "/task.json")
                        .param("flowFile", "/flow.xml"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loaded").value(true))
                .andExpect(jsonPath("$.steps[0].id").value("step1"));
    }

    @Test
    void loadFlowShouldReturnBadRequestOnError() throws Exception {
        when(flowExecutionService.loadFlow(any(), any()))
                .thenThrow(new RuntimeException("bad config"));

        mockMvc.perform(post("/api/flow/load")
                        .param("taskFile", "/bad.json")
                        .param("flowFile", "/bad.xml"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.loaded").value(false))
                .andExpect(jsonPath("$.errorMessage").value("bad config"));
    }

    @Test
    void startFlowShouldReturnStarted() throws Exception {
        doNothing().when(flowExecutionService).startFlow();

        mockMvc.perform(post("/api/flow/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("started"));
    }

    @Test
    void startFlowShouldReturnBadRequestWhenNotLoaded() throws Exception {
        doThrow(new IllegalStateException("Flow not loaded"))
                .when(flowExecutionService).startFlow();

        mockMvc.perform(post("/api/flow/start"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Flow not loaded"));
    }

    @Test
    void getStatusShouldReturnCurrentStatus() throws Exception {
        FlowStatus status = new FlowStatus();
        status.setLoaded(true);
        status.setRunning(true);
        when(flowExecutionService.getStatus()).thenReturn(status);

        mockMvc.perform(get("/api/flow/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.running").value(true));
    }

    @Test
    void getLogsShouldReturnLogLines() throws Exception {
        when(nodeLogStore.getLogs("myStep"))
                .thenReturn(List.of("line one", "line two"));

        mockMvc.perform(get("/api/flow/logs/myStep"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("line one"))
                .andExpect(jsonPath("$[1]").value("line two"));
    }
}
