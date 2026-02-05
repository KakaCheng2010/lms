package com.ruoyi.lms.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.lms.domain.CourseTeacher;
import com.ruoyi.lms.dto.CourseTeacherDto;
import com.ruoyi.lms.service.ICourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程教师关联Controller
 */
@RestController
@RequestMapping("/lms/courseTeacher")
public class CourseTeacherController extends BaseController {

    @Autowired
    private ICourseTeacherService courseTeacherService;

    /**
     * 查询课程教师关联列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CourseTeacher courseTeacher) {
        startPage();
        List<CourseTeacher> list = courseTeacherService.selectList(courseTeacher);
        return getDataTable(list);
    }

    /**
     * 获取课程教师关联详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(courseTeacherService.getById(id));
    }

    /**
     * 新增课程教师关联
     */
    @PostMapping
    public AjaxResult add(@RequestBody CourseTeacher courseTeacher) {
        return toAjax(courseTeacherService.save(courseTeacher));
    }

    /**
     * 修改课程教师关联
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CourseTeacher courseTeacher) {
        return toAjax(courseTeacherService.updateById(courseTeacher));
    }

    /**
     * 删除课程教师关联
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(courseTeacherService.removeByIds(java.util.Arrays.asList(ids)));
    }
    
    /**
     * 根据年级和班级查询老师及科目信息
     */
    @GetMapping("/teachersByGradeAndClazz")
    public AjaxResult getTeachersByGradeAndClazz(CourseTeacherDto courseTeacher) {
        List<CourseTeacher> teachers = courseTeacherService.selectTeachersByGradeAndClazz(courseTeacher);
        return AjaxResult.success(teachers);
    }
}

