package com.ruoyi.lms.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.lms.dto.CourseDto;
import com.ruoyi.lms.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lms/course")
public class CourseController extends BaseController {

    @Autowired
    private ICourseService courseService;

    @GetMapping("/list")
    public List<CourseDto> list(CourseDto dto) {
       // startPage();
        List<CourseDto> list = courseService.selectList(dto);
        //return getDataTable(list);
        return list;
    }

    @PostMapping("saveBatch")
    public AjaxResult saveBatch(@RequestBody CourseDto dto) {
        Integer count = courseService.saveBatch(dto);
        return AjaxResult.success(count);
    }


    @GetMapping("/checkCourse")
    public List<CourseDto> checkCourse(CourseDto dto) {
        List<CourseDto> list = courseService.checkCourse(dto);
        return list;
    }

    @GetMapping("/checkTeacherConflict")
    public List<CourseDto> checkTeacherConflict(CourseDto dto) {
        List<CourseDto> conflicts = courseService.checkTeacherConflict(dto.getTerm(), dto.getGrade());
        return conflicts;
    }

}
