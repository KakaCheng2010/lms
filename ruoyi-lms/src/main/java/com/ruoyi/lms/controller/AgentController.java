package com.ruoyi.lms.controller;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.action.InterruptionMetadata;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/lms/agent")
public class AgentController {

    @Autowired
    private ReactAgent reactAgent;

    @PostMapping(value = "/schedule-stream")
    public Flux scheduleCourseStream(@RequestParam("query") String query,
                                     @RequestParam("threadId") String threadId) throws GraphRunnerException {

        // 线程配置（用于记忆 / 会话状态）
        RunnableConfig config = RunnableConfig.builder()
                .threadId(threadId)
                .build();

        // 获取 NodeOutput 流
        Flux<NodeOutput> nodeStream = reactAgent.stream(query, config);

        // 转换为前端友好的 SSE 字符串流
        return nodeStream
                .filter(nodeOutput -> nodeOutput instanceof StreamingOutput)
                .map(nodeOutput -> (StreamingOutput) nodeOutput)
                .flatMap(streaming -> {
                    OutputType type = streaming.getOutputType();

                    if (type == OutputType.AGENT_MODEL_STREAMING) {
                        // 模型正在生成 token（最常见、最重要的部分）
                        String delta = streaming.message().getText();
                        if (delta != null && !delta.isEmpty()) {
                            return Flux.just("data: " + delta + "\n\n");
                        }
                    } else if (type == OutputType.AGENT_MODEL_FINISHED) {
                        // 一轮模型调用完成
                        return Flux.just("data: [思考完成]\n\n");
                    } else if (type == OutputType.AGENT_TOOL_FINISHED) {
                        // 工具调用完成（可选显示）
                        return Flux.just("data: [工具执行完成: ]\n\n");
                    }
                    // 其他类型可根据需要添加或忽略
                    return Flux.empty();
                })
                .onErrorResume(e -> {
                    // 错误时返回错误信息
                    return Flux.just("data: [错误] " + e.getMessage() + "\n\n");
                })
                .concatWith(Flux.just("data: [stream-end]\n\n"));  // 标记流结束
    }

}