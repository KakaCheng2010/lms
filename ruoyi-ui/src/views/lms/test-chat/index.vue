<template>
  <div class="test-container">
    <div class="header">
      <h2>智能排课助手测试页面</h2>
      <p>点击下方按钮打开聊天对话框</p>
    </div>
    
    <div class="button-container">
      <el-button type="primary" size="large" @click="openChat">
        <i class="el-icon-chat-dot-round"></i>
        打开智能排课助手
      </el-button>
    </div>

    <!-- 聊天对话框组件 -->
    <ChatDialog ref="chatDialog" />
  </div>
</template>

<script>
import ChatDialog from '@/views/lms/chat/index.vue';

export default {
  name: 'TestChatPage',
  components: {
    ChatDialog
  },
  data() {
    return {};
  },
  methods: {
    openChat() {
      this.$refs.chatDialog.showDialog();
    }
  }
};
</script>

<style lang="scss" scoped>
.test-container {
  padding: 30px;
  max-width: 800px;
  margin: 0 auto;
  text-align: center;

  .header {
    margin-bottom: 40px;

    h2 {
      color: #303133;
      margin-bottom: 10px;
    }

    p {
      color: #606266;
      font-size: 16px;
    }
  }

  .button-container {
    margin: 40px 0;

    .el-button {
      padding: 20px 40px;
      font-size: 16px;

      i {
        margin-right: 10px;
      }
    }
  }
}
</style>