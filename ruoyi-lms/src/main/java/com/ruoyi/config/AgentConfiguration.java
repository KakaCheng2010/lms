package com.ruoyi.config;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.ruoyi.lms.tools.ReadCourseCountTool;
import com.ruoyi.lms.tools.ReadTeacherTool;
import com.ruoyi.lms.tools.WriteCourseTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfiguration {

    @Bean
    public ReactAgent reactAgent(ChatModel chatModel, ReadCourseCountTool readCountTool, ReadTeacherTool readTeacherTool, WriteCourseTool writeCourseTool){

        String systemPrompt = """
                你说一名排课的教师，只能使用提供的工具來进行排课。
               
                排课的实现流程
                1.根据给定的年级，使用读取教师授课表工具（查询时将用户要求的年级改为数字，比如 一年级 是1 ，二年级是2），查询教师授课表，分配课程的时候要符合教师的授课科目，教师所在的班级，如果没有返回，则证明没有限制，分配时不必分配教师。
                2.根据给定的年级，使用读取科目节次表工具（查询时将用户要求的年级改为数字，比如 一年级 是1 ，二年级是2），查询科目节次表，分配课程的时候要符合课程每周安排的节次，注意只分配有限制的课程。
                3.满足用户提出的其他要求
                4.根据上述要求自动分配课程表，如果没有对应的教师则空着，只安排课程，分配完成使用写入课程信息工具将数据插入数据库 （插入时进行数据转换，例如：一年级为1 二年级是2 ， 1班 是1 2班是2  2026年第一学期 20261 2025年第二学期 20252）
      
                """;

        return ReactAgent.builder()
                .name("course agent")
                .description("这是一个用于排课的助手 ")
                .model(chatModel)
                .systemPrompt(systemPrompt)
                .saver(new MemorySaver())
                .tools(
                        readCountTool.toolCallback(),
                        readTeacherTool.toolCallback(),
                        writeCourseTool.toolCallback()
                )
                .build();
    }




}