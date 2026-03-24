package com.huangyunkun.acpsure.web.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.huangyunkun.acpsure.core.util.ApplicationAwareUtil;
import com.huangyunkun.acpsure.web.service.NodeLogStore;

/**
 * Logback appender that captures log messages per LiteFlow node.
 * When the MDC key "nodeId" is set, log messages are stored in {@link NodeLogStore}.
 */
public class NodeLogAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        String nodeId = event.getMDCPropertyMap().get("nodeId");
        if (nodeId == null || nodeId.isBlank()) {
            return;
        }
        try {
            NodeLogStore store = ApplicationAwareUtil.getBean(NodeLogStore.class);
            store.addLog(nodeId, event.getFormattedMessage());
        } catch (Exception ignored) {
            // NodeLogStore might not be available during startup; ignore silently
        }
    }
}
