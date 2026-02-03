package com.ruoyi.lms.mapper;

import java.util.List;

import com.ruoyi.lms.domain.CourseCount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程数量Mapper接口
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
@Mapper
public interface CourseCountMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<CourseCount> {
    /**
     * 查询课程数量
     * 
     * @param courseCount 课程数量
     * @return 课程数量集合
     */
    public java.util.List<CourseCount> selectCourseCountList(CourseCount courseCount);

    /**
     * 批量删除课程数量
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCourseCountByIds(Long[] ids);

}