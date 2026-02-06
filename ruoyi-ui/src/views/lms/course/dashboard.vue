<template>
  <div class="app-container" v-loading="loading">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="学期" prop="term">
        <el-select v-model="queryParams.term" placeholder="学期" @change="handleQuery">
          <el-option
              v-for="dict in dict.type.term"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="年级" prop="grade">
        <el-select v-model="queryParams.grade" placeholder="年级" @change="handleQuery">
          <el-option
              v-for="dict in dict.type.grade"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <!--      <el-form-item>-->
      <!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
      <!--      </el-form-item>-->
      
      <el-form-item>
        <el-button 
          type="success" 
          icon="el-icon-download" 
          size="mini" 
          @click="handleExport"
          :loading="exportLoading">
          导出Excel
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border>
      <!-- 节次列 -->
      <el-table-column
          prop="classPeriod"
          label="节次"
          fixed
          align="center"
      ></el-table-column>

      <!-- 动态星期 -->
      <el-table-column
          v-for="day in week_days"
          :key="day.key"
          :label="day.label"
          align="center"
      >
        <!-- 动态班级 -->
        <el-table-column
            v-for="clazz in dict.type.clazz"
            :key="`${day.value}-${clazz.value}`"
            :label="clazz.label"
            align="center"
        >
          <template slot-scope="scope">
            <div v-if="scope.row[day.key] && scope.row[day.key][clazz.value] && scope.row[day.key][clazz.value].course">
              <dict-tag :options="dict.type.course" :value="scope.row[day.key][clazz.value].course"/>
              {{ scope.row[day.key][clazz.value].teacher }}
            </div>
            <div v-else>--</div>
          </template>
        </el-table-column>
      </el-table-column>
    </el-table>

    <el-row>
      <el-col :span="11">
        <el-divider content-position="left">课时不匹配展示</el-divider>

        <el-table :data="checkTable" border>
          <el-table-column prop="clazz" label="班级">


          </el-table-column>

          <el-table-column prop="course" label="课程">

            <template slot-scope="scope">
              <dict-tag :options="dict.type.course" :value="scope.row.course"/>
            </template>

          </el-table-column>

          <el-table-column prop="courseSum" label="课时">

          </el-table-column>

          <el-table-column prop="courseSumStandard" label="标准课时">

          </el-table-column>

        </el-table>
      </el-col>
      <el-col :span="12" offset="1">
        <el-divider content-position="left">教师安排冲突展示</el-divider>

        <el-table :data="teacherConflictTable" border>
          <el-table-column prop="clazz" label="班级" width="100">
          </el-table-column>

          <el-table-column prop="course" label="课程" width="100">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.course" :value="scope.row.course"/>
            </template>
          </el-table-column>

          <el-table-column prop="teacher" label="教师" width="100">
          </el-table-column>

          <el-table-column prop="weekDay" label="周几" width="80">
            <template slot-scope="scope">
              星期{{ scope.row.weekDay }}
            </template>
          </el-table-column>

          <el-table-column prop="classPeriod" label="第几节" width="100">
          </el-table-column>

          <el-table-column prop="conflictDescription" label="冲突描述" min-width="200">
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import {list, checkCourse, checkTeacherConflict, exportCourseSchedule} from "@/api/lms/course";

export default {
  dicts: ['term', 'clazz', 'course', 'grade'],
  data() {
    return {
      loading: false,
      exportLoading: false, // 导出加载状态
      // 显示搜索条件
      showSearch: true,
      // 查询参数
      queryParams: {
        term: '20251',
        grade: '1',
      },
      checkTable: [],
      teacherConflictTable: [], // 教师冲突表格数据
      // 核心数据
      tableData: [],
      week_days: [
        {key: 1, label: '星期一'},
        {key: 2, label: '星期二'},
        {key: 3, label: '星期三'},
        {key: 4, label: '星期四'},
        {key: 5, label: '星期五'}
      ],
      class_period: [1, 2, 3, 4, 5, 6],
    };
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
      this.getCheckList()
      this.getTeacherConflictList()
    },
    //根据节次获取行
    getRow(classPeriod) {
      let rows = this.tableData.filter(item => {
        return item.classPeriod == classPeriod;
      })

      if (rows.length == 1) {
        return rows[0]
      }
    },
    getCheckList() {
      this.checkTable = []
      checkCourse(this.queryParams).then(response => {
        this.checkTable = response.filter(item => {
          return item.courseSum != item.courseSumStandard
        })
      })
    },

    getTeacherConflictList() {
      this.teacherConflictTable = []
      checkTeacherConflict(this.queryParams).then(response => {
        this.teacherConflictTable = response;
      })
    },

    getList() {
      //班级
      this.getDicts("clazz").then(response => {
        let clazzList = response.data
        //节次
        this.tableData = this.class_period.map(num => {
          const row = {
            classPeriod: num
          };
          //星期
          this.week_days.forEach(day => {
            const week = {}
            //班级
            clazzList.forEach(clazz => {
              week[clazz.dictValue] = {course: '', teacher: ''}
            })
            row[day.key] = week
          })
          return row;
        })

        list(this.queryParams).then(response => {
          response.forEach(row => {
            let periodObj = this.getRow(row.classPeriod)
            periodObj[row.weekDay][row.clazz].course = row.course
            periodObj[row.weekDay][row.clazz].teacher = row.teacher
          })

          this.loading = false;
        });

      })
    },

    /** 导出按钮操作 */
    handleExport() {
      // 检查必要参数
      if (!this.queryParams.term || !this.queryParams.grade) {
        this.$modal.msgError("请选择学期和年级");
        return;
      }

      this.exportLoading = true;
      
      // 构建导出URL
      const params = new URLSearchParams({
        term: this.queryParams.term,
        grade: this.queryParams.grade
      });
      
      const exportUrl = `${process.env.VUE_APP_BASE_API}/lms/course/export?${params.toString()}`;
      
      // 创建隐藏的下载链接
      const link = document.createElement('a');
      link.href = exportUrl;
      link.style.display = 'none';
      
      // 添加到DOM并触发下载
      document.body.appendChild(link);
      link.click();
      
      // 清理DOM
      document.body.removeChild(link);
      
      // 延迟重置加载状态，给用户反馈
      setTimeout(() => {
        this.exportLoading = false;
        this.$modal.msgSuccess("导出请求已发送，请稍候下载");
      }, 1000);
    },
  },
  mounted() {
    this.getList()
    this.getCheckList()
    this.getTeacherConflictList()
  }
};
</script>
