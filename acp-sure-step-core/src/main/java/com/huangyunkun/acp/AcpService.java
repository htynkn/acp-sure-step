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
            for (String arg : args) {
                agentBuilder.arg(arg);
            }
        }
        if (MapUtils.isNotEmpty(envMap)) {
            agentBuilder.env(envMap);
        }
        var params = agentBuilder.build();
        var transport = new StdioAcpClientTransport(params);
        transport.setStdErrorHandler(err -> System.err.println("[AcpService][stderr] " + err));

        client = AcpClient.sync(transport)
                .clientCapabilities(new AcpSchema.ClientCapabilities(new AcpSchema.FileSystemCapability(true, true), true))
                .sessionUpdateConsumer(notification -> {
                    if (notification.update() instanceof AcpSchema.AgentMessageChunk msg
                            && msg.content() instanceof AcpSchema.TextContent textContent) {
                        System.out.print(textContent.text());
                    }
                })
                .build();

        System.out.println("[AcpService] Initializing client with command: " + command);
        client.initialize();
        System.out.println("[AcpService] Client initialized, creating session...");
        var session = client.newSession(new AcpSchema.NewSessionRequest(workSpace, List.of()));
        sessionId = session.sessionId();
        System.out.println("[AcpService] Session created, sessionId: " + sessionId);
    }

    public AcpSchema.PromptResponse exec(String promptText) {
        System.out.println("[AcpService] Executing prompt (sessionId=" + sessionId + "): " + promptText);
        var response = client.prompt(new AcpSchema.PromptRequest(
                sessionId,
                List.of(new AcpSchema.TextContent(promptText))
        ));
        System.out.println("[AcpService] Prompt response stopReason: " + response.stopReason());
        return response;
    }
}
