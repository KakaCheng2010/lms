import request from '@/utils/request'

// 查询参数列表
export function list(query) {
  return request({
    url: '/lms/course/list',
    method: 'get',
    params: query
  })
}

export function checkCourse(query) {
  return request({
    url: '/lms/course/checkCourse',
    method: 'get',
    params: query
  })
}

//保存并上传数据
export function saveBatch(data) {
  return request({
    url: 'lms/course/saveBatch',
    method: 'post',
    data: data
  })
}

// 检查教师冲突
export function checkTeacherConflict(query) {
  return request({
    url: '/lms/course/checkTeacherConflict',
    method: 'get',
    params: query
  })
}

// 导出课程表
export function exportCourseSchedule(query) {
  return request({
    url: '/lms/course/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

