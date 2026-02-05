<template>
  <div class="chat-container">
    <el-dialog
      title="智能排课助手"
      :visible.sync="dialogVisible"
      width="80%"
      :before-close="handleClose"
      class="chat-dialog"
      :modal-append-to-body="false"
    >
      <div class="chat-wrapper">
        <!-- 消息区域 -->
        <div class="messages-container" ref="messagesContainer">
          <div 
            v-for="(message, index) in messages" 
            :key="index" 
            :class="['message', message.role]"
          >
            <div class="message-avatar">
              <i 
                :class="message.role === 'user' ? 'el-icon-user-solid' : 'el-icon-chat-line-round'"
                :style="{ color: message.role === 'user' ? '#409EFF' : '#67C23A' }"
              ></i>
            </div>
            <div class="message-content">
              <div class="message-text" v-html="formatMessage(message.content)"></div>
              <div class="message-time">{{ message.timestamp | formatDate }}</div>
            </div>
          </div>
          
          <!-- 加载动画 -->
          <div v-if="loading" class="message ai">
            <div class="message-avatar">
              <i class="el-icon-chat-line-round" style="color: #67C23A;"></i>
            </div>
            <div class="message-content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="input-container">
          <el-input
            v-model="inputMessage"
            :rows="4"
            type="textarea"
            placeholder="请输入您的问题..."
            maxlength="1000"
            show-word-limit
            @keyup.enter.native="sendMessage"
            :disabled="loading"
          />
          <div class="send-button-container">
            <el-button 
              type="primary" 
              @click="sendMessage" 
              :disabled="!inputMessage.trim() || loading"
              :loading="loading"
            >
              {{ loading ? '发送中...' : '发送' }}
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from "@/utils/auth";

export default {
  name: 'ChatDialog',
  data() {
    return {
      dialogVisible: true,
      messages: [],
      inputMessage: '',
      loading: false,
      threadId: null,
      baseUrl: process.env.VUE_APP_BASE_API || '/api'
    };
  },
  filters: {
    formatDate(timestamp) {
      return new Date(timestamp).toLocaleTimeString('zh-CN');
    }
  },
  mounted() {
    // 生成唯一会话ID
    this.threadId = this.generateThreadId();
    this.inputMessage='帮我生成1年级、2026年上学期的课程表，1年级共3个班， 每周5天 ，每天有6节课。要求：1.语文数学尽量放到第 1 2 3 4节课，2.两节相同的课尽量不要排在一起'
  },
  methods: {
    // 显示对话框
    showDialog() {
      this.dialogVisible = true;
      this.messages = [];
    },

    // 关闭对话框
    handleClose(done) {
      this.dialogVisible = false;
      this.messages = [];
      this.inputMessage = '';
      this.loading = false;
      done();
    },

    // 发送消息
    async sendMessage() {
      if (!this.inputMessage.trim() || this.loading) {
        return;
      }

      const userMessage = {
        role: 'user',
        content: this.inputMessage,
        timestamp: new Date().getTime()
      };

      // 添加用户消息到列表
      this.messages.push(userMessage);
      const messageToSend = this.inputMessage;
      this.inputMessage = '';

      // 滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom();
      });

      // 显示加载状态
      this.loading = true;

      try {
        // 调用后端SSE接口
        await this.callScheduleStream(messageToSend);
      } catch (error) {
        console.error('Error sending message:', error);
        this.messages.push({
          role: 'ai',
          content: `发生错误: ${error.message || '未知错误'}`,
          timestamp: new Date().getTime()
        });
      } finally {
        this.loading = false;
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },

    // 调用后端流式API
    async callScheduleStream(query) {
      // 保存Vue实例的引用
      const self = this;
      
      let aiResponse = {
        role: 'ai',
        content: '',
        timestamp: new Date().getTime()
      };

      // 添加AI消息占位符
      const aiMessageIndex = this.messages.length;
      this.messages.push(aiResponse);

      return new Promise((resolve, reject) => {
        const url = `${this.baseUrl}/lms/agent/schedule-stream?query=${encodeURIComponent(query)}&threadId=${this.threadId}`;
        
        fetchEventSource(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: "Bearer " + getToken()
          },
          onopen(response) {
            if (response.ok) {
              console.log('Connection established');
            } else {
              console.error(`HTTP error! Status: ${response.status}`);
              reject(new Error(`HTTP error! Status: ${response.status}`));
            }
          },
          onmessage(event) {
            if (event.data === '[stream-end]' || event.data === 'data: [stream-end]') {
              // 流结束
              console.log('Stream ended');
              resolve();
              return;
            }

            if (event.data.includes('[错误]')) {
              // 处理错误信息
              const errorMessage = event.data.replace('data: [错误] ', '').replace('[错误] ', '');
              aiResponse.content += errorMessage;
              
              // 更新消息内容
              self.$set(self.messages, aiMessageIndex, aiResponse);
              return;
            }

            if (event.data.includes('[思考完成]')) {
              // 可以选择性地显示思考完成状态
              aiResponse.content += '\n[AI正在处理...]';
              
              // 更新消息内容
              self.$set(self.messages, aiMessageIndex, aiResponse);
              self.$nextTick(() => {
                self.scrollToBottom();
              });
              return;
            }

            if (event.data.includes('[工具执行完成:]')) {
              // 可以选择性地显示工具执行完成状态
              aiResponse.content += '\n[工具执行完成]';
              
              // 更新消息内容
              self.$set(self.messages, aiMessageIndex, aiResponse);
              self.$nextTick(() => {
                self.scrollToBottom();
              });
              return;
            }

            // 正常的消息内容
            if (event.data.startsWith('data: ') && event.data !== 'data: \n\n') {
              let content = event.data.substring(6); // 移除 'data: ' 前缀
              
              // 避免重复内容
              if (content && content !== '\n\n' && content !== '[stream-end]\n\n') {
                // 如果内容以换行符结尾，移除它以避免多余的空行
                if (content.endsWith('\n\n')) {
                  content = content.substring(0, content.length - 2);
                }
                
                aiResponse.content += content;
                
                // 更新消息内容
                self.$set(self.messages, aiMessageIndex, aiResponse);
                
                // 滚动到底部
                self.$nextTick(() => {
                  self.scrollToBottom();
                });
              }
            }
          },
          onclose() {
            console.log('Connection closed by server');
            resolve();
          },
          onerror(err) {
            console.error('EventSource failed:', err);
            reject(err);
          }
        });
      });
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    },

    // 生成线程ID
    generateThreadId() {
      return 'thread_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    },

    // 格式化消息内容
    formatMessage(content) {
      // 将换行符转换为HTML <br> 标签
      return content.replace(/\n/g, '<br>');
    }
  }
};
</script>

<style lang="scss" scoped>
.chat-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 9999;
}

.chat-dialog {
  .chat-wrapper {
    display: flex;
    flex-direction: column;
    height: 60vh;
  }

  .messages-container {
    flex: 1;
    overflow-y: auto;
    padding: 20px;
    background-color: #f5f7fa;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    margin-bottom: 15px;
    max-height: 400px;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: #f1f1f1;
    }

    &::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb:hover {
      background: #a8a8a8;
    }
  }

  .message {
    display: flex;
    margin-bottom: 15px;
    align-items: flex-start;

    &.user {
      justify-content: flex-end;
      
      .message-avatar {
        order: 2;
        margin-left: 10px;
      }

      .message-content {
        background-color: #ecf5ff;
        border: 1px solid #d9ecff;
        border-radius: 18px 0 18px 18px;
        max-width: 80%;
      }
    }

    &.ai {
      justify-content: flex-start;

      .message-content {
        background-color: #f4f4f5;
        border: 1px solid #e9e9eb;
        border-radius: 0 18px 18px 18px;
        max-width: 80%;
      }
    }
  }

  .message-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    border: 1px solid #ebeef5;
  }

  .message-content {
    padding: 12px 16px;
    word-wrap: break-word;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  }

  .message-text {
    font-size: 14px;
    line-height: 1.5;
    color: #333;
  }

  .message-time {
    font-size: 12px;
    color: #909399;
    text-align: right;
    margin-top: 5px;
  }

  .typing-indicator {
    display: flex;
    align-items: center;
    padding: 8px 0;

    span {
      width: 8px;
      height: 8px;
      background-color: #67C23A;
      border-radius: 50%;
      display: inline-block;
      margin-right: 4px;
      animation: typing 1s infinite;

      &:nth-child(2) {
        animation-delay: 0.2s;
      }

      &:nth-child(3) {
        animation-delay: 0.4s;
      }
    }
  }

  @keyframes typing {
    0%, 100% {
      transform: translateY(0);
    }
    50% {
      transform: translateY(-5px);
    }
  }

  .input-container {
    position: relative;

    .el-textarea {
      ::v-deep .el-textarea__inner {
        resize: vertical;
        min-height: 100px !important;
      }
    }
  }

  .send-button-container {
    margin-top: 10px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>