package com.ruoyi.lms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.dto.CourseDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface ICourseService extends IService<Course> {
    List<CourseDto> selectList(CourseDto dto);

    Integer saveBatch(CourseDto domain);

    /**
     * 单个保存
     *
     * @param domain
     * @return
     */
    List<CourseDto> checkCourse(CourseDto domain);
    
    void clearSchedule(String grade, String clazz, String term);
    
    /**
     * 检查教师冲突
     *
     * @param term 学期
     * @param grade 年级
     * @return 教师冲突信息列表
     */
    List<CourseDto> checkTeacherConflict(String term, String grade);
    
    /**
     * 导出课程表到Excel
     *
     * @param term 学期
     * @param grade 年级
     * @param outputStream 输出流
     * @throws IOException IO异常
     */
    void exportCourseScheduleToExcel(String term, String grade, OutputStream outputStream) throws IOException;
}
