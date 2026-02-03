package com.ruoyi.lms.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("lms_course_count")
public class CourseCount extends BaseEntity {
    /**
     * 主键 ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long id;

    /**
     * 年级
     */
    @TableField("grade")
    private String grade;

    /**
     * 课程
     */
    @TableField("course")
    private String course;

    /**
     * 每周课程数
     */
    @TableField("course_sum")
    private Integer courseSum;
}