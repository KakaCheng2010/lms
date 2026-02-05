import request from '@/utils/request'

// 发送消息
export function sendChatMessage(data) {
  return request({
    url: '/lms/agent/schedule-stream',
    method: 'post',
    data: data
  })
}