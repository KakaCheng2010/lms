package com.ruoyi.lms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.lms.convert.CourseConvert;
import com.ruoyi.lms.convert.CourseScheduleExportConverter;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.dto.CourseDto;
import com.ruoyi.lms.dto.CourseScheduleExportRow;
import com.ruoyi.lms.mapper.CourseMapper;
import com.ruoyi.lms.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private CourseScheduleExportConverter exportConverter;

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

    @Override
    public List<CourseDto> checkTeacherConflict(String term, String grade) {
        // 查询指定学期和年级的所有课程
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term", term);
        if (grade != null && !grade.isEmpty()) {
            queryWrapper.eq("grade", grade);
        }
        
        List<Course> courses = this.baseMapper.selectList(queryWrapper);
        
        // 使用Map存储周几+节次+教师的组合，值为对应的课程列表
        Map<String, List<Course>> scheduleMap = new HashMap<>();
        
        // 遍历课程，按周几+节次+教师分组
        for (Course course : courses) {
            String key = course.getWeekDay() + "_" + course.getClassPeriod() + "_" + course.getTeacher();
            scheduleMap.computeIfAbsent(key, k -> new ArrayList<>()).add(course);
        }
        
        // 查找冲突：同一时间（周几+节次）同一个教师被分配给多个班级
        List<CourseDto> conflicts = new ArrayList<>();
        for (Map.Entry<String, List<Course>> entry : scheduleMap.entrySet()) {
            List<Course> courseList = entry.getValue();
            
            // 如果同一时间同一教师对应多于1个班级，则存在冲突
            if (courseList.size() > 1) {
                for (Course course : courseList) {
                    CourseDto conflict = CourseConvert.INSTANCE.convert2Dto(course);
                    conflict.setHasConflict(true);
                    conflict.setConflictDescription(
                        "教师 " + course.getTeacher() + " 在周" + course.getWeekDay() + 
                        " 第" + course.getClassPeriod() + " 节同时教授多个班级");
                    conflicts.add(conflict);
                }
            }
        }
        
        return conflicts;
    }

    @Override
    public void exportCourseScheduleToExcel(String term, String grade, OutputStream outputStream) throws IOException {
        // 查询课程数据
        CourseDto queryDto = new CourseDto();
        queryDto.setTerm(term);
        queryDto.setGrade(grade);
        List<CourseDto> courses = selectList(queryDto);
        
        // 提取班级列表
        List<String> classes = exportConverter.extractClasses(courses);
        
        // 如果没有班级数据，创建默认班级列表
        if (classes.isEmpty()) {
            classes.add("1班");
            classes.add("2班");
            classes.add("3班");
        }
        
        // 转换为导出数据
        List<CourseScheduleExportRow> exportData = exportConverter.convertToExportData(courses, classes);
        
        // 创建表头数据
        List<List<String>> headData = createExcelHeaders(classes);
        
        // 创建数据行
        List<List<Object>> dataRows = createDataRows(exportData, classes);
        
        // 使用EasyExcel写入数据
        EasyExcel.write(outputStream)
                .head(headData)
                .sheet("课程表")
                .doWrite(dataRows);
    }
    
    /**
     * 创建Excel表头
     * @param classes 班级列表
     * @return 表头数据
     */
    private List<List<String>> createExcelHeaders(List<String> classes) {
        List<List<String>> headData = new ArrayList<>();
        
        // 第一列：节次
        headData.add(List.of("节次", "节次"));
        
        // 为每个星期的每个班级创建列
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五"};
        for (String weekDay : weekDays) {
            for (String clazz : classes) {
                headData.add(List.of(weekDay, clazz));
            }
        }
        
        return headData;
    }
    
    /**
     * 创建数据行
     * @param exportData 导出数据
     * @param classes 班级列表
     * @return 数据行列表
     */
    private List<List<Object>> createDataRows(List<CourseScheduleExportRow> exportData, List<String> classes) {
        List<List<Object>> dataRows = new ArrayList<>();
        
        for (CourseScheduleExportRow row : exportData) {
            List<Object> dataRow = new ArrayList<>();
            
            // 第一列：节次
            dataRow.add(row.getClassPeriod());
            
            // 为每个星期的每个班级添加数据
            for (int weekDay = 1; weekDay <= 5; weekDay++) {
                for (String clazz : classes) {
                    dataRow.add(row.getCourseInfo(weekDay, clazz));
                }
            }
            
            dataRows.add(dataRow);
        }
        
        return dataRows;
    }

}
