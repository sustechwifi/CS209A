<template>
<div>
  <el-menu
      class="el-menu-vertical-demo"
      style="width: 200px;min-height: calc(100vh - 50px);background-color: #eddcab"
      default-active="home"
      router
  >
    <el-menu-item index="home">
      <el-icon><house /></el-icon>
      <span>Home Page</span>
    </el-menu-item>

    <el-menu-item index="add">
      <el-icon><upload /></el-icon>
      <span>upload data</span>
    </el-menu-item>

    <el-menu-item index="search">
      <el-icon><search /></el-icon>
      <span>query & DML</span>
    </el-menu-item>

    <el-menu-item index="dml">
      <el-icon><EditPen /></el-icon>
      <span>Batch DML</span>
    </el-menu-item>

    <el-menu-item index="advance">
      <el-icon><document /></el-icon>
      <span>special query</span>
    </el-menu-item>

  </el-menu>
</div>
</template>
<script>
import request from "../../util/request";

export default {
  name: "Aside",
  created() {
    this.init();
  },
  data(){
    return{
      isUser: true,
      user:{
        id:1,
        username:'',
        nickName:'',
        isAdmin:false,
        img:"",
      },
    }
  },
  methods:{
    init(){
      let t = window.localStorage.getItem('token')
      request.get('/user/me',{
        params:{
          token: t
        }
      }).then(res => {
        if (res.code === '0'){
          //console.log(res.data);
          this.user = JSON.parse(res.data);
          this.isUser = this.user.isAdmin === 'false';
        }
      })
    },
  }
}
</script>

<style scoped>

</style>