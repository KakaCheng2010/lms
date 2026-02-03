package com.ruoyi.lms.service.impl;

import com.ruoyi.RuoYiApplication;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.scheduler.AutoSchedulingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RuoYiApplication.class)
public class AutoSchedulingTest {

    @Autowired
    private AutoSchedulingService autoSchedulingService;

    @Test
    public void testGenerateSchedule() {
        // 准备测试数据
        String grade = "一年级";
        String clazz = "一班";
        String term = "2025春季";
        int maxClassPerDay = 6;
        int weekDays = 5;

        // 定义课程和教师的映射关系
        Map<String, String> courseTeachers = new HashMap<>();
        courseTeachers.put("语文", "张老师");
        courseTeachers.put("数学", "李老师");
        courseTeachers.put("英语", "王老师");
        courseTeachers.put("物理", "赵老师");
        courseTeachers.put("化学", "孙老师");

        // 生成课程表
        List<Course> schedule = autoSchedulingService.generateSchedule(grade, clazz, term, courseTeachers, maxClassPerDay, weekDays);

        // 验证结果
        assert !schedule.isEmpty() : "排课结果不应为空";
        System.out.println("成功生成 " + schedule.size() + " 条课程安排");

        // 输出部分结果
        for (int i = 0; i < Math.min(10, schedule.size()); i++) {
            Course course = schedule.get(i);
            System.out.printf("课程: %s, 教师: %s, 时间: 周%d 第%d节%n", 
                course.getCourse(), course.getTeacher(), 
                course.getWeekDay(), course.getClassPeriod());
        }
    }

    @Test
    public void testGenerateOptimizedSchedule() {
        // 准备测试数据
        String grade = "二年级";
        String clazz = "二班";
        String term = "2025春季";
        int maxClassPerDay = 6;
        int weekDays = 5;

        // 定义课程和教师的映射关系
        Map<String, String> courseTeachers = new HashMap<>();
        courseTeachers.put("语文", "张老师");
        courseTeachers.put("数学", "李老师");
        courseTeachers.put("英语", "王老师");
        courseTeachers.put("物理", "赵老师");
        courseTeachers.put("化学", "孙老师");
        courseTeachers.put("生物", "周老师");
        courseTeachers.put("历史", "吴老师");

        // 生成优化课程表
        List<Course> schedule = autoSchedulingService.generateOptimizedSchedule(
            grade, clazz, term, courseTeachers, maxClassPerDay, weekDays);

        // 验证结果
        assert !schedule.isEmpty() : "优化排课结果不应为空";
        System.out.println("成功生成 " + schedule.size() + " 条优化课程安排");

        // 输出结果统计
        Map<String, Integer> courseCount = new HashMap<>();
        for (Course course : schedule) {
            courseCount.merge(course.getCourse(), 1, Integer::sum);
        }

        System.out.println("各课程安排统计:");
        for (Map.Entry<String, Integer> entry : courseCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " 节");
        }
    }
}