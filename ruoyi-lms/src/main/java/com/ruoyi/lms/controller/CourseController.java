package com.ruoyi.lms.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.dto.CourseDto;
import com.ruoyi.lms.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
}
