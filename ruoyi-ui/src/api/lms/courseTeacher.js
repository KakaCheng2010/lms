import request from '@/utils/request'

// 查询课程教师关联列表
export function listCourseTeacher(query) {
  return request({
    url: '/lms/courseTeacher/list',
    method: 'get',
    params: query
  })
}

// 查询课程教师关联详细
export function getCourseTeacher(id) {
  return request({
    url: '/lms/courseTeacher/' + id,
    method: 'get'
  })
}

// 新增课程教师关联
export function addCourseTeacher(data) {
  return request({
    url: '/lms/courseTeacher',
    method: 'post',
    data: data
  })
}

// 修改课程教师关联
export function updateCourseTeacher(data) {
  return request({
    url: '/lms/courseTeacher',
    method: 'put',
    data: data
  })
}

// 删除课程教师关联
export function delCourseTeacher(id) {
  return request({
    url: '/lms/courseTeacher/' + id,
    method: 'delete'
  })
}



export function teachersByGradeAndClazz(query) {
  return request({
    url: '/lms/courseTeacher/teachersByGradeAndClazz',
    method: 'get',
    params: query
  })
}
