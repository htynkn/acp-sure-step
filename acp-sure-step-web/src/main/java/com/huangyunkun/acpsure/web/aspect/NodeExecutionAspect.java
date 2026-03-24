package com.huangyunkun.acpsure.web.aspect;

import com.huangyunkun.acpsure.web.service.FlowExecutionService;
import com.yomahub.liteflow.core.NodeComponent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * AOP aspect that intercepts LiteFlow node execution to:
 * <ul>
 *   <li>Track which step is currently running in {@link FlowExecutionService}</li>
 *   <li>Set the SLF4J MDC "nodeId" key so that {@link com.huangyunkun.acpsure.web.logging.NodeLogAppender}
 *       can route log messages to the correct per-step log store</li>
 * </ul>
 */
@Aspect
@Component
public class NodeExecutionAspect {

    private final FlowExecutionService flowExecutionService;

    public NodeExecutionAspect(FlowExecutionService flowExecutionService) {
        this.flowExecutionService = flowExecutionService;
    }

    @Around("execution(* com.yomahub.liteflow.core.NodeComponent+.process())")
    public Object aroundNodeProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        NodeComponent node = (NodeComponent) joinPoint.getTarget();
        String nodeId = node.getNodeId();

        MDC.put("nodeId", nodeId);
        flowExecutionService.nodeStarted(nodeId);
        try {
            Object result = joinPoint.proceed();
            flowExecutionService.nodeCompleted(nodeId);
            return result;
        } catch (Throwable t) {
            flowExecutionService.nodeFailed(nodeId, t.getMessage());
            throw t;
        } finally {
            MDC.remove("nodeId");
        }
    }
}
