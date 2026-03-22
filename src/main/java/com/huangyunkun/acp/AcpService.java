package com.huangyunkun.acp;

import com.agentclientprotocol.sdk.client.AcpClient;
import com.agentclientprotocol.sdk.client.AcpSyncClient;
import com.agentclientprotocol.sdk.client.transport.AgentParameters;
import com.agentclientprotocol.sdk.client.transport.StdioAcpClientTransport;
import com.agentclientprotocol.sdk.spec.AcpSchema;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Map;

public class AcpService {
    private AcpSyncClient client;
    private String sessionId;

    public void init(String command, List<String> args, Map<String, String> envMap, String workSpace) {
        AgentParameters.Builder agentBuilder = AgentParameters.builder(command);
        if (CollectionUtils.isNotEmpty(args)) {
            agentBuilder.args(args);
        }
        if (MapUtils.isNotEmpty(envMap)) {
            agentBuilder.env(envMap);
        }
        var params = agentBuilder.build();
        var transport = new StdioAcpClientTransport(params);

        client = AcpClient.sync(transport)
                .sessionUpdateConsumer(notification -> {
                    if (notification.update() instanceof AcpSchema.AgentMessageChunk msg) {
                        System.out.print(((AcpSchema.TextContent) msg.content()).text());
                    }
                })
                .build();

        client.initialize();
        var session = client.newSession(new AcpSchema.NewSessionRequest(workSpace, List.of()));
        sessionId = session.sessionId();
    }

    public AcpSchema.PromptResponse exec(String promptText) {
        return client.prompt(new AcpSchema.PromptRequest(
                sessionId,
                List.of(new AcpSchema.TextContent(promptText))
        ));
    }
}
