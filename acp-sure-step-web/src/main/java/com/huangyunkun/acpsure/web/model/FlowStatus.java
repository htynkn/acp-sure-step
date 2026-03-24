package com.huangyunkun.acpsure.web.model;

import lombok.Data;

import java.util.List;

@Data
public class FlowStatus {
    private boolean loaded;
    private boolean running;
    private boolean completed;
    private boolean failed;
    private String errorMessage;
    private List<StepInfo> steps;
}
