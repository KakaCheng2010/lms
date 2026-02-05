package com.ruoyi.lms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.lms.domain.CourseTeacher;

import java.util.List;

public interface ICourseTeacherService extends IService<CourseTeacher> {

    List<CourseTeacher> selectList(CourseTeacher courseTeacher);
}