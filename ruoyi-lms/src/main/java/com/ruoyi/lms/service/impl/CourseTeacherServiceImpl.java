package com.ruoyi.lms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.lms.domain.CourseTeacher;
import com.ruoyi.lms.dto.CourseTeacherDto;
import com.ruoyi.lms.mapper.CourseTeacherMapper;
import com.ruoyi.lms.service.ICourseTeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {

    @Override
    public List<CourseTeacher> selectList(CourseTeacher courseTeacher) {
        QueryWrapper<CourseTeacher> queryWrapper = new QueryWrapper<>();
        if (courseTeacher.getGrade() != null && !courseTeacher.getGrade().equals("")) {
            queryWrapper.eq("grade", courseTeacher.getGrade());
        }
        if (courseTeacher.getClazz() != null && !courseTeacher.getClazz().equals("")) {
            queryWrapper.eq("clazz", courseTeacher.getClazz());
        }
        if (courseTeacher.getCourse() != null && !courseTeacher.getCourse().equals("")) {
            queryWrapper.eq("course", courseTeacher.getCourse());
        }
        if (courseTeacher.getTeacher() != null && !courseTeacher.getTeacher().equals("")) {
            queryWrapper.like("teacher", courseTeacher.getTeacher());
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<CourseTeacher> selectTeachersByGradeAndClazz(CourseTeacherDto dto) {
        QueryWrapper<CourseTeacher> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(dto.getGrade())) {
            queryWrapper.eq("grade", dto.getGrade());
        }
        if (StringUtils.isNotEmpty(dto.getClazz())) {
            queryWrapper.eq("clazz", dto.getClazz());
        }
        
        return this.baseMapper.selectList(queryWrapper);
    }
}