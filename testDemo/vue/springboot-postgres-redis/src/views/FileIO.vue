<template>
  <div class="common-layout" style="padding: 100px">
    <el-container>
      <el-header style="margin: auto">
        <h1>FileIO Test</h1>
      </el-header>
      <el-divider/>

      <div style="margin: auto">
        <el-alert style="margin: auto" title="-- see results in the terminal --" type="info" />
        <el-button type="success" @click="load">Load Files</el-button>
        <el-button type="info" @click="dml">DML Test</el-button>
        <el-button type="primary" @click="query">query</el-button>
        <el-input v-model="itemName"></el-input>
        <br>
        <br>
        <el-alert style="margin: auto" title="------- advanced query ------" type="info" />
      </div>

      <div class="demo-collapse" style="height: 80%">
        <el-collapse>
          <el-collapse-item title="task1" name="1">
            <div>
              <h3>find the oldest containers</h3>
              <hr>
              <el-button type="primary" size="default" @click="task1">search</el-button>
            </div>
          </el-collapse-item>
          <el-collapse-item title="task2" name="2">
            <div>
              <h3>find the most hard-working courier by company and city</h3>
              <hr>
            </div>
            <el-row>
              <el-button size="default" type="primary" @click="task2">search</el-button>
            </el-row>
          </el-collapse-item>
          <el-collapse-item title="task3" name="3">
            <div>
              <h3>find the cities that has the least cost of export</h3>
              <hr>
            </div>
            <div>
              <el-button size="default" type="primary" @click="task3">search</el-button>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-container>
  </div>
</template>

<script>
import request from "../../util/request";

export default {
  name: "FileIO",
  methods:{
    load(){
      request.get("/file/fileIO").then(res=>{
        if (res.code === '0'){
          this.$message({
            type:'success',
            message:res.data
          })
        }
      })
    },
    dml(){
      request.get("/file/dml").then(res=>{
        if (res.code === '0'){
          this.$message({
            type:'success',
            message:res.data
          })
        }
      })
    },
    query(){
      request.get("/file/find",{
        params:{
          itemName: this.itemName
        }
      }).then(res=>{
        if (res.code === '0'){
          this.$message({
            type:'success',
            message:res.data
          })
        }
      })
    },
    task1(){
      request.get("/file/task1").then(res=>{
        if (res.code === '0'){
          this.$message({
            type:'success',
            message:res.data
          })
        }
        console.log(res);
      })
    },
    task2(){
      request.get("/file/task2").then(res=>{
        if (res.code === '0'){
          this.$message({
            type:'success',
            message:res.data
          })
        }
        console.log(res);
      })
    },
    task3(){
      request.get("/file/task3").then(res=>{
        if (res.code === '0'){
          this.$message({
            type:'success',
            message:res.data
          })
        }
        console.log(res);
      })
    },
  },
  data(){
    return{
      city:'',
      company:'',
      container:{
        code:'',
        type:'',
      },
      itemName:''
    }
  }
}
</script>

<style scoped>

</style>