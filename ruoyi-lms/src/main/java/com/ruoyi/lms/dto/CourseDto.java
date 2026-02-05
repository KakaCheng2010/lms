package com.ruoyi.lms.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto extends BaseDto{
    /**
     * 主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     * 年
     */
    private String grade;
    /**
     * 班级
     */
    private String clazz;

    /**
     * 课程
     */
    private String course;

    /**
     * 教师
     */
    private String teacher;

    /**
     * 学期
     */
    private String term;

    /**
     * 周几
     */
    private Integer weekDay;

    /**
     * 节次
     */
    private Integer classPeriod;

    /**
     * 课程信息
     */
    private List<CourseDto> courseDtoList;


    private Integer courseSum;

    private Integer courseSumStandard;
    
    /**
     * 是否存在冲突
     */
    private Boolean hasConflict;
    
    /**
     * 冲突描述
     */
    private String conflictDescription;
}
