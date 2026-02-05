package com.ruoyi.lms.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.lms.convert.CourseConvert;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.dto.CourseDto;
import com.ruoyi.lms.mapper.CourseMapper;
import com.ruoyi.lms.service.ICourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Override
    public List<CourseDto> selectList(CourseDto dto) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>();

        if (dto.getGrade() != null && !dto.getGrade().equals("")) {
            queryWrapper.eq("grade", dto.getGrade());
        }

        if (dto.getClazz() != null && !dto.getClazz().equals("")) {
            queryWrapper.eq("clazz", dto.getClazz());
        }

        if (dto.getTerm() != null && !dto.getTerm().equals("")) {
            queryWrapper.eq("term", dto.getTerm());
        }

        List<Course> villages = this.baseMapper.selectList(queryWrapper);
        List<CourseDto> villageDtoList = CourseConvert.INSTANCE.convert2ListDto(villages);
        return villageDtoList;
    }

    @Override
    public Integer saveBatch(CourseDto domain) {
        //删除旧数据
        UpdateWrapper<Course> updateWrapper = new UpdateWrapper<Course>();
        updateWrapper.eq("grade", domain.getGrade());
        updateWrapper.eq("clazz", domain.getClazz());
        updateWrapper.eq("term", domain.getTerm());
        this.baseMapper.delete(updateWrapper);

        //保存新数据
        domain.getCourseDtoList().forEach(dto -> {
            dto.setId(IdUtil.getSnowflakeNextId());
            this.baseMapper.insert(CourseConvert.INSTANCE.convert2Entity(dto));
        });

        return 0;
    }

    @Override
    public List<CourseDto> checkCourse(CourseDto domain) {
        List<CourseDto> gradeDto = this.baseMapper.selectCourseList(domain.getTerm(), domain.getGrade());
        return gradeDto;
    }

    @Override
    public void clearSchedule(String grade, String clazz, String term) {
        // 删除指定条件下的课程安排
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Course> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Course>();
        queryWrapper.eq("grade", grade);
        queryWrapper.eq("clazz", clazz);
        queryWrapper.eq("term", term);
        
        this.baseMapper.delete(queryWrapper);
    }

}
