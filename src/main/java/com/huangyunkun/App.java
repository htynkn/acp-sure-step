package com.huangyunkun;

import com.yomahub.liteflow.builder.LiteFlowNodeBuilder;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;
import com.yomahub.liteflow.slot.DefaultContext;

public class App {
    public static void main(String[] args) {
        LiteFlowNodeBuilder.createCommonNode().setId("a")
                .setName("组件A")
                .setClazz("com.huangyunkun.AcpNode")
                .build();

        LiteFlowNodeBuilder.createCommonNode().setId("b")
                .setName("组件B")
                .setClazz("com.huangyunkun.AcpNode")
                .build();


        LiteflowConfig config = new LiteflowConfig();
        config.setRuleSource("config.xml");
        FlowExecutor flowExecutor = FlowExecutorHolder.loadInstance(config);


        StepInputData stepInputData = new StepInputData();
        stepInputData.setName("world");
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", stepInputData, DefaultContext.class);

        System.out.println(response);
    }
}
