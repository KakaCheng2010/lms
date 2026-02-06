package com.ruoyi.lms.convert;

import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.lms.dto.CourseDto;
import com.ruoyi.lms.dto.CourseScheduleExportRow;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程表导出数据转换器
 * 负责将CourseDto列表转换为Excel导出格式的数据结构
 */
@Component
public class CourseScheduleExportConverter {
    
    /**
     * 将CourseDto列表转换为Excel导出行数据
     * @param courses 课程数据列表
     * @param classes 班级列表
     * @return Excel导出行数据列表
     */
    public List<CourseScheduleExportRow> convertToExportData(List<CourseDto> courses, List<String> classes) {
        // 创建6个节次的导出行
        List<CourseScheduleExportRow> exportRows = new ArrayList<>();
        for (int period = 1; period <= 6; period++) {
            exportRows.add(new CourseScheduleExportRow("第" + period + "节"));
        }
        
        // 确保所有班级都被初始化为"--"
        for (CourseScheduleExportRow row : exportRows) {
            for (String clazz : classes) {
                for (int weekDay = 1; weekDay <= 5; weekDay++) {
                    row.setCourseInfo(weekDay, clazz, "--");
                }
            }
        }
        
        // 填充实际的课程数据
        for (CourseDto course : courses) {
            if (course.getClassPeriod() != null && course.getWeekDay() != null 
                && course.getClassPeriod() >= 1 && course.getClassPeriod() <= 6
                && course.getWeekDay() >= 1 && course.getWeekDay() <= 5) {
                
                CourseScheduleExportRow row = exportRows.get(course.getClassPeriod() - 1);
                String courseInfo = formatCourseInfo(course.getCourse(), course.getTeacher());
                row.setCourseInfo(course.getWeekDay(), course.getClazz(), courseInfo);
            }
        }
        
        return exportRows;
    }
    
    /**
     * 格式化课程信息为"课程中文名称 教师姓名"格式
     * @param courseValue 课程字典值
     * @param teacherName 教师姓名
     * @return 格式化后的课程信息
     */
    private String formatCourseInfo(String courseValue, String teacherName) {
        if (courseValue == null || courseValue.trim().isEmpty()) {
            return "--";
        }
        
        // 使用DictUtils将课程字典值转换为中文标签
        String courseName = DictUtils.getDictLabel("course", courseValue.trim());
        if (courseName == null || courseName.trim().isEmpty()) {
            // 如果字典转换失败，使用原始值
            courseName = courseValue.trim();
        }
        
        if (teacherName == null || teacherName.trim().isEmpty()) {
            return courseName;
        }
        return courseName + " " + teacherName.trim();
    }
    
    /**
     * 从课程数据中提取所有班级列表
     * @param courses 课程数据列表
     * @return 排序后的班级列表
     */
    public List<String> extractClasses(List<CourseDto> courses) {
        Set<String> classSet = courses.stream()
                .map(CourseDto::getClazz)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        List<String> classList = new ArrayList<>(classSet);
        // 按班级名称排序（数字优先）
        classList.sort((a, b) -> {
            try {
                // 尝试按数字排序
                Integer numA = Integer.parseInt(a.replaceAll("[^0-9]", ""));
                Integer numB = Integer.parseInt(b.replaceAll("[^0-9]", ""));
                return numA.compareTo(numB);
            } catch (NumberFormatException e) {
                // 如果不是数字，按字符串排序
                return a.compareTo(b);
            }
        });
        
        return classList;
    }
    
    /**
     * 生成导出文件名
     * @param term 学期（如"20251"）
     * @param grade 年级（如"1"）
     * @return 文件名（如"2025年第1学期1年级课程表.xlsx"）
     */
    public String generateFileName(String term, String grade) {
        if (term == null || term.length() < 5) {
            throw new IllegalArgumentException("Invalid term format: " + term);
        }
        
        try {
            // 解析学期：前4位是年份，第5位是学期
            String year = term.substring(0, 4);
            String semester = term.substring(4, 5);
            
            return String.format("%s年第%s学期%s年级课程表.xlsx", year, semester, grade);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse term: " + term, e);
        }
    }
}