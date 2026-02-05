package com.ruoyi.lms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.lms.domain.Course;
import com.ruoyi.lms.dto.CourseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Select("select tt.*, t2.course_sum as courseSumStandard from  " +
            "(select t1.clazz,t1.course,count(*) as courseSum from lms_course t1 WHERE t1.grade=#{grade} and t1.term=#{term} GROUP BY t1.clazz,t1.course) tt " +
            "left join lms_course_count t2 on t2.grade=#{grade} and tt.course=t2.course")
    public List<CourseDto> selectCourseList(@Param("term") String term,@Param("grade") String grade);
}
