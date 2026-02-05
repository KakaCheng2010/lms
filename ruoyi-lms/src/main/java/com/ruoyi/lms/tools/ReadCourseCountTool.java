package com.ruoyi.lms.tools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.ruoyi.lms.domain.CourseCount;
import com.ruoyi.lms.service.ICourseCountService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReadCourseCountTool implements Tool<ReadCourseCountTool.Request, List<CourseCount>> {

    @Autowired
    private ICourseCountService courseCountService;

    @Override
    public ToolCallback toolCallback() {
        return FunctionToolCallback.builder("读取科目节次表，返回某个科目每周的节次数", this)
                .description("根据年级读取科目节次表，返回科目名称，每周的节次表")
                .inputType(ReadCourseCountTool.Request.class)
                .build();
    }

    @Override
    public  List<CourseCount> apply(Request request, ToolContext toolContext) {

        CourseCount courseCount = new CourseCount();
        if (request.grade != null && !request.grade.isEmpty()) {
            courseCount.setGrade(request.grade);
        }

        return courseCountService.selectCourseCountList(courseCount);
    }

    @JsonClassDescription("读取科目节次表，返回某个科目每周的节次数,参数为年级")
    public record Request(
            @JsonProperty(value = "grade", required = true)
            @JsonPropertyDescription("年级")
            String grade
    ) {}
}
