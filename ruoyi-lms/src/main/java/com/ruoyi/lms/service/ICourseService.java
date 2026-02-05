package com.ruoyi.lms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.dto.CourseDto;

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
}
