package com.huangyunkun.acpsure.web.model;

import lombok.Data;

@Data
public class StepInfo {
    private String id;
    private StepState state;
    private String errorMessage;

    public StepInfo(String id) {
        this.id = id;
        this.state = StepState.PENDING;
    }
}
