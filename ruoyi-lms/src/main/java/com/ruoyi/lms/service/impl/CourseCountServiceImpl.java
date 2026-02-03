package com.ruoyi.lms.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.lms.domain.CourseCount;
import com.ruoyi.lms.mapper.CourseCountMapper;
import com.ruoyi.lms.service.ICourseCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 课程数量Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
@Service
public class CourseCountServiceImpl extends ServiceImpl<CourseCountMapper, CourseCount> implements ICourseCountService {
    @Autowired
    private CourseCountMapper courseCountMapper;

    /**
     * 查询课程数量
     * 
     * @param courseCount 课程数量
     * @return 课程数量集合
     */
    @Override
    public List<CourseCount> selectCourseCountList(CourseCount courseCount)
    {
        return courseCountMapper.selectCourseCountList(courseCount);
    }

    /**
     * 查询课程数量
     * 
     * @param id 课程数量主键
     * @return 课程数量
     */
    @Override
    public CourseCount selectCourseCountById(Long id)
    {
        return courseCountMapper.selectById(id);
    }

    /**
     * 新增课程数量
     * 
     * @param courseCount 课程数量
     * @return 结果
     */
    @Override
    public int insertCourseCount(CourseCount courseCount)
    {
        return courseCountMapper.insert(courseCount);
    }

    /**
     * 修改课程数量
     * 
     * @param courseCount 课程数量
     * @return 结果
     */
    @Override
    public int updateCourseCount(CourseCount courseCount)
    {
        return courseCountMapper.updateById(courseCount);
    }

    /**
     * 批量删除课程数量
     * 
     * @param ids 需要删除的课程数量主键集合
     * @return 结果
     */
    @Override
    public int deleteCourseCountByIds(Long[] ids)
    {
        return courseCountMapper.deleteCourseCountByIds(ids);
    }

    /**
     * 删除课程数量信息
     * 
     * @param id 课程数量主键
     * @return 结果
     */
    @Override
    public int deleteCourseCountById(Long id)
    {
        return courseCountMapper.deleteById(id);
    }
}