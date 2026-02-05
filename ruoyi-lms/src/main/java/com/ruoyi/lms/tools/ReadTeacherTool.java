package com.ruoyi.lms.tools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.ruoyi.lms.domain.CourseTeacher;
import com.ruoyi.lms.dto.CourseTeacherDto;
import com.ruoyi.lms.service.ICourseTeacherService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReadTeacherTool implements Tool<ReadTeacherTool.Request, List<CourseTeacher>> {

    @Autowired
    private ICourseTeacherService courseTeacherService;

    @Override
    public ToolCallback toolCallback() {
        return FunctionToolCallback.builder("读取教师授课表", this)
                .description("根据年级读取教师授课表，返回教师所授的科目 所在的年级 班级信息")
                .inputType(ReadTeacherTool.Request.class)
                .build();
    }


    @Override
    public  List<CourseTeacher> apply(Request request, ToolContext toolContext) {

        CourseTeacher courseTeacher = new CourseTeacher();
        if (request.grade != null && !request.grade.isEmpty()) {
            courseTeacher.setGrade(request.grade);
        }

        return courseTeacherService.selectList(courseTeacher);
    }

    @JsonClassDescription("读取教师授课表，返回教师所授的科目 所在的年级 班级信息，参数为年级")
    public record Request(
            @JsonProperty(value = "grade", required = true)
            @JsonPropertyDescription("年级")
            String grade
    ) {}
}
