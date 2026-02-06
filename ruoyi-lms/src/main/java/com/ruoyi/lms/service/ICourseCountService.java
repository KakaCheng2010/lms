package com.ruoyi.lms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.lms.domain.CourseCount;

import java.util.List;

public interface ICourseCountService extends IService<CourseCount> {
    /**
     * 查询课程数量
     *
     * @param courseCount 课程数量
     * @return 课程数量集合
     */
    List<CourseCount> selectCourseCountList(CourseCount courseCount);

    /**
     * 查询课程数量
     *
     * @param id 课程数量主键
     * @return 课程数量
     */
    CourseCount selectCourseCountById(Long id);

    /**
     * 新增课程数量
     *
     * @param courseCount 课程数量
     * @return 结果
     */
    int insertCourseCount(CourseCount courseCount);

    /**
     * 修改课程数量
     *
     * @param courseCount 课程数量
     * @return 结果
     */
    int updateCourseCount(CourseCount courseCount);

    /**
     * 批量删除课程数量
     *
     * @param ids 需要删除的课程数量主键集合
     * @return 结果
     */
    int deleteCourseCountByIds(Long[] ids);

    /**
     * 删除课程数量信息
     *
     * @param id 课程数量主键
     * @return 结果
     */
    int deleteCourseCountById(Long id);
}