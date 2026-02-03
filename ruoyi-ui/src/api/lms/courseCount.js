import request from '@/utils/request'

// 查询课程数量列表
export function listCourseCount(query) {
  return request({
    url: '/lms/courseCount/list',
    method: 'get',
    params: query
  })
}

// 查询课程数量详细
export function getCourseCount(id) {
  return request({
    url: '/lms/courseCount/' + id,
    method: 'get'
  })
}

// 新增课程数量
export function addCourseCount(data) {
  return request({
    url: '/lms/courseCount',
    method: 'post',
    data: data
  })
}

// 修改课程数量
export function updateCourseCount(data) {
  return request({
    url: '/lms/courseCount',
    method: 'put',
    data: data
  })
}

// 删除课程数量
export function delCourseCount(id) {
  return request({
    url: '/lms/courseCount/' + id,
    method: 'delete'
  })
}