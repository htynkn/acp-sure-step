package com.huangyunkun;

import com.yomahub.liteflow.core.NodeComponent;

public class AcpNode extends NodeComponent {
    @Override
    public void process() throws Exception {
        StepInputData contextBean = this.getRequestData();
        System.out.println("hello " + contextBean.getName());
    }
}
