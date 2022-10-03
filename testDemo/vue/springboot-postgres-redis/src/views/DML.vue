<template>
  <div class="common-layout" style="padding: 100px">
    <el-container>
      <el-header style="margin: auto">
        <h1>Batch Data Manipulation Language Test</h1>
      </el-header>
      <span style="margin: auto">
          <h4>valid record count:{{this.totalCount}}</h4>
        </span>
      <el-divider />
      <div class="demo-collapse" style="height: 80%">
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="delete" name="1">
            <div>
              <h3>delete records</h3>
              <hr>
              <el-input style="width: 300px;padding-right: 40px" v-model="idCnt" placeholder="input the number of delete count"></el-input>
              <el-button size="default" type="danger" @click="deleteByIds">deleteAll</el-button>
            </div>
          </el-collapse-item>
          <el-collapse-item title="insert" name="1">
            <div>
              <h3>insert</h3>
              <hr>
            </div>
          </el-collapse-item>
          <el-collapse-item title="update" name="1">
            <div>
              <h3>update</h3>
              <hr>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-container>
  </div>
</template>

<script>
import {ElNotification} from "element-plus";
import request from "../../util/request";

export default {
  name: "DML",
  mounted() {
    ElNotification.success({
      title: 'DML test',
      message: 'welcome to DML test',
      offset: 100,
    })
    request.get("/dml/").then(res => {
      this.totalCount = res.data;
      console.log(res)
    })
  },
  methods:{
    activeNames(){

    },
    handleChange(){

    },
    deleteByIds(){
      let ids = [];
      for (let i = 0;i < this.idCnt;i++){
        ids[i] = Math.random()*100;
      }
      request.post("/dml/deleteByIds",ids).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功,"+res.msg
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
  },
  data(){
    return{
      idCnt:"",
      totalCount:0,
    }
  }
}
</script>

<style scoped>

</style>