package com.ruoyi.lms.tools;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.service.ICourseService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClearCourseTool implements Tool<ClearCourseTool.Request, String > {

    @Autowired
    private ICourseService courseService;

    @Override
    public ToolCallback toolCallback() {
        return FunctionToolCallback.builder("清除课程信息", this)
                .description("清除课程信息")
                .inputType(ClearCourseTool.Request.class)
                .build();
    }


    @Override
    public String apply(Request request, ToolContext toolContext) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade", request.grade);
        courseService.remove(queryWrapper);
        return "数据保存成功";
    }

    @JsonClassDescription("清除课程信息")
    public record Request(
            @JsonProperty(value = "grade", required = true)
            @JsonPropertyDescription("年级")
            String grade
    ) {
    }
}
