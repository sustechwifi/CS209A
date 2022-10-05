<template>
  <div class="common-layout" style="padding: 100px">
    <el-container>
      <el-header style="margin: auto">
        <h1>Batch Data Manipulation Language Test</h1>
      </el-header>
      <span style="margin: auto">
          <h4>valid record count:{{ this.totalCount }}</h4>
        </span>
      <el-divider/>
      <div class="demo-collapse" style="height: 80%">
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="delete" name="1">
            <div>
              <h3>delete records</h3>
              <hr>
              <el-row>
                <el-input style="width: 300px;" v-model="idCnt"
                          placeholder="input the number of delete count"></el-input>
                <div style="margin : auto">
                  <el-button size="default" type="danger" @click="deleteByIds">deleteAll</el-button>
                </div>
              </el-row>

              <el-row style="padding-top: 20px">
                <el-input style="width: 100px;" v-model="idStart" placeholder="from"></el-input>
                <h2>--</h2>
                <el-input style="width: 100px;" v-model="idEnd" placeholder="to"></el-input>
                <div style="margin : auto">
                  <el-button size="default" type="danger" @click="deleteByRangeIds">deleteRange</el-button>
                </div>
              </el-row>

            </div>
          </el-collapse-item>
          <el-collapse-item title="insert" name="2">
            <div>
              <h3>insert</h3>
              <hr>
            </div>
            <el-row>
              <el-input style="width: 300px;padding-right: 30px" v-model="insertBody.insertPath"
                        placeholder="input the path of .csv file"></el-input>
              <el-upload ref="uploadRef" class="upload-demo" action="" :auto-upload="false">
                <template #trigger>
                  <el-button type="primary" size="default">Browse files</el-button>
                </template>
              </el-upload>
            </el-row>
            <el-row style="padding-top: 20px">
              <el-input style="width: 100px;" v-model="insertBody.lineStart" placeholder="from"></el-input>
              <h2>--</h2>
              <el-input style="width: 100px;" v-model="insertBody.lineEnd" placeholder="to"></el-input>
              <div style="margin : auto">
                <el-button size="default" type="danger" @click="insertByRange">lineRange</el-button>
              </div>
            </el-row>
          </el-collapse-item>
          <el-collapse-item title="update" name="3">
            <div>
              <h3>update</h3>
              <hr>
            </div>
            <a href="/search">click here</a> to query & dml
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
    this.getCount();
  },
  methods: {
    getCount() {
      request.get("/dml/").then(res => {
        this.totalCount = res.data;
        console.log(res)
      })
    },
    activeNames() {

    },
    handleChange() {

    },
    deleteByIds() {
      let ids = [];
      for (let i = 0; i < this.idCnt; i++) {
        ids[i] = Math.random() * 100;
      }
      request.post("/dml/deleteByIds", ids).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功," + res.msg
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.getCount();
      })
    },
    deleteByRangeIds() {
      let ids = [];
      for (let i = this.insertBody.idStart; i < this.insertBody.idEnd; i++) {
        ids[i] = i;
      }
      request.post("/dml/deleteByIds", ids).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功," + res.msg
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.getCount();
      })
    },
    insertByRange() {
      request.post("/dml/insert", this.insertBody).then(res => {
        console.log(this.insertBody)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "insert successfully" + res.data
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.getCount();
      })
    }
  },
  data() {
    return {
      idCnt: "",
      totalCount: 0,
      idStart: "",
      idEnd: "",
      insertBody:{
        insertPath:"",
        lineStart: "",
        lineEnd: "",
      }
    }
  }
}
</script>

<style scoped>

</style>