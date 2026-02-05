package com.ruoyi.lms.tools;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.domain.CourseTeacher;
import com.ruoyi.lms.service.ICourseService;
import com.ruoyi.lms.service.ICourseTeacherService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WriteCourseTool implements Tool<WriteCourseTool.Request, String > {

    @Autowired
    private ICourseService courseService;

    @Override
    public ToolCallback toolCallback() {
        return FunctionToolCallback.builder("写入课程信息", this)
                .description("写入课程信息")
                .inputType(WriteCourseTool.Request.class)
                .build();
    }


    @Override
    public String apply(Request request, ToolContext toolContext) {

        Course course = new Course();
        course.setId(IdUtil.getSnowflakeNextId());
        course.setGrade(request.grade);
        course.setClazz(request.clazz);
        course.setCourse(request.course);
        course.setTeacher(request.teacher);
        course.setTerm(request.term);
        course.setWeekDay(request.weekDay);
        course.setClassPeriod(request.classPeriod);

        courseService.saveOrUpdate(course);
        return "数据保存成功";
    }

    @JsonClassDescription("写入课程信息")
    public record Request(
            @JsonProperty(value = "grade", required = true)
            @JsonPropertyDescription("年级")
            String grade,

            @JsonProperty(value = "clazz", required = true)
            @JsonPropertyDescription("班级")
            String clazz,

            @JsonProperty(value = "course", required = true)
            @JsonPropertyDescription("科目")

            String course,

            @JsonProperty(value = "teacher", required = true)
            @JsonPropertyDescription("教师")
            String teacher,

            @JsonProperty(value = "term", required = true)
            @JsonPropertyDescription("学期")
            String term,

            @JsonProperty(value = "weekDay", required = true)
            @JsonPropertyDescription("星期几")
            Integer weekDay,

            @JsonProperty(value = "classPeriod", required = true)
            @JsonPropertyDescription("节次")
            Integer classPeriod

    ) {
    }
}
