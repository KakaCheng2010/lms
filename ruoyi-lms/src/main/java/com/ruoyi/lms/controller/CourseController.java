package com.ruoyi.lms.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.lms.convert.CourseScheduleExportConverter;
import com.ruoyi.lms.dto.CourseDto;
import com.ruoyi.lms.service.ICourseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/lms/course")
public class CourseController extends BaseController {

    @Autowired
    private ICourseService courseService;
    
    @Autowired
    private CourseScheduleExportConverter exportConverter;

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

    /**
     * 导出课程表Excel文件
     * @param term 学期
     * @param grade 年级
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void exportCourseSchedule(@RequestParam String term, 
                                   @RequestParam String grade,
                                   HttpServletResponse response) throws IOException {
        try {
            // 生成文件名
            String fileName = exportConverter.generateFileName(term, grade);
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            
            // 防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName);
            
            // 调用服务层生成Excel文件
            courseService.exportCourseScheduleToExcel(term, grade, response.getOutputStream());
            
        } catch (Exception e) {
            // 发生异常时返回错误信息
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{\"code\":500,\"msg\":\"Excel导出失败：" + e.getMessage() + "\"}");
        }
    }


}
