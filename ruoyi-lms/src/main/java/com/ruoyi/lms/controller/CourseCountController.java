package com.ruoyi.lms.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.lms.domain.CourseCount;
import com.ruoyi.lms.service.ICourseCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程数量Controller
 * 
 * @author ruoyi
 * @date 2026-02-03
 */
@RestController
@RequestMapping("/lms/courseCount")
public class CourseCountController extends BaseController
{
    @Autowired
    private ICourseCountService courseCountService;

    /**
     * 查询课程数量列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CourseCount courseCount)
    {
        startPage();
        List<CourseCount> list = courseCountService.selectCourseCountList(courseCount);
        return getDataTable(list);
    }

    /**
     * 获取课程数量详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(courseCountService.selectCourseCountById(id));
    }

    /**
     * 新增课程数量
     */
    @PostMapping
    public AjaxResult add(@RequestBody CourseCount courseCount)
    {
        return toAjax(courseCountService.insertCourseCount(courseCount));
    }

    /**
     * 修改课程数量
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CourseCount courseCount)
    {
        return toAjax(courseCountService.updateCourseCount(courseCount));
    }

    /**
     * 删除课程数量
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(courseCountService.deleteCourseCountByIds(ids));
    }
}