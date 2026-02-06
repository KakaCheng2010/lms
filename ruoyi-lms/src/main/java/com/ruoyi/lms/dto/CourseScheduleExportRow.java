package com.ruoyi.lms.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 课程表导出行数据模型
 * 用于定义Excel导出的行结构，每行代表一个节次的课程安排
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseScheduleExportRow {
    
    /**
     * 节次（第1节、第2节...第6节）
     */
    @ExcelProperty("节次")
    private String classPeriod;
    
    /**
     * 周一各班级课程安排
     * Key: 班级名称（如"1班"、"2班"）
     * Value: 课程信息（如"语文 张老师"或"--"）
     */
    private Map<String, String> mondayClasses;
    
    /**
     * 周二各班级课程安排
     */
    private Map<String, String> tuesdayClasses;
    
    /**
     * 周三各班级课程安排
     */
    private Map<String, String> wednesdayClasses;
    
    /**
     * 周四各班级课程安排
     */
    private Map<String, String> thursdayClasses;
    
    /**
     * 周五各班级课程安排
     */
    private Map<String, String> fridayClasses;
    
    /**
     * 构造函数，初始化所有Map
     */
    public CourseScheduleExportRow(String classPeriod) {
        this.classPeriod = classPeriod;
        this.mondayClasses = new HashMap<>();
        this.tuesdayClasses = new HashMap<>();
        this.wednesdayClasses = new HashMap<>();
        this.thursdayClasses = new HashMap<>();
        this.fridayClasses = new HashMap<>();
    }
    
    /**
     * 根据星期几获取对应的班级课程Map
     * @param weekDay 星期几（1-5）
     * @return 对应星期的班级课程Map
     */
    public Map<String, String> getClassesByWeekDay(Integer weekDay) {
        switch (weekDay) {
            case 1: return mondayClasses;
            case 2: return tuesdayClasses;
            case 3: return wednesdayClasses;
            case 4: return thursdayClasses;
            case 5: return fridayClasses;
            default: throw new IllegalArgumentException("Invalid weekDay: " + weekDay);
        }
    }
    
    /**
     * 设置指定星期和班级的课程信息
     * @param weekDay 星期几（1-5）
     * @param clazz 班级名称
     * @param courseInfo 课程信息
     */
    public void setCourseInfo(Integer weekDay, String clazz, String courseInfo) {
        getClassesByWeekDay(weekDay).put(clazz, courseInfo);
    }
    
    /**
     * 获取指定星期和班级的课程信息
     * @param weekDay 星期几（1-5）
     * @param clazz 班级名称
     * @return 课程信息，如果没有则返回"--"
     */
    public String getCourseInfo(Integer weekDay, String clazz) {
        return getClassesByWeekDay(weekDay).getOrDefault(clazz, "--");
    }
}