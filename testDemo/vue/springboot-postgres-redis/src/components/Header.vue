<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc ; background-color: lightgray;
  display: flex">
    <div style="width: 60%;padding-left: 30px;font-weight: bold;color: darkslateblue">SUSTC 管理系统</div>
    <div style="flex: 1"></div>
    <div style="width: 200px">
        <span class="el-avatar el-avatar--circle" style="height: 40px; width: 40px; line-height: 40px;">
        <img :src="this.user.img" style="object-fit: cover;">
      </span>
      <el-dropdown>
    <span class="el-dropdown-link" style="padding-top: 15px">
      <el-tag class="ml-2" type="warning">{{this.user.nickName}}({{this.info}})</el-tag>
    </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/personHome')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import request from "../../util/request";

export default {
  name: "Header",
  created() {
    this.init();
  },
  data(){
    return{
      info:"",
      user:{
        id:0,
        username:'',
        nickName:'',
        admin:false,
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
          console.log(res.data);
          this.user = JSON.parse(res.data);
          this.info = (this.user.admin === 'true') ? "管理员":"用户"
        }else {
          this.$message({
            type : "error",
            message : res.msg
          })
          this.$router.push('/login');
        }
      })
    },
    logout(){
      let t = window.localStorage.getItem('token');
      if (t !== null){
        request.get('/user/logout',{
          params:{
            token: t
          }
        })
        window.localStorage.removeItem("token");
      }
      this.$message({
        type : "success",
        message : "退出成功"
      })
      this.$router.push("/login")
    }
  }
}
</script>

<style scoped>

</style>