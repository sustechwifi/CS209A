<template>
  <div class="login-container"
       v-bind:style="{backgroundImage:'url(' + bg + ')',
        backgroundRepeat:'no-repeat',
        backgroundSize:'100% 100%',}"
       style=" overflow: hidden; height: 100vh"
       onclick="location.reload([false])"
  >
    <div style="width: 400px;margin: 150px auto"></div>
    <div style="color: white; font-size: 30px; text-align: center">登录</div>

    <div class="dialog-wrapper">
      <el-dialog
          v-model="loginDialogVisible"
          title="Tips"
          width="30%"
          append-to-body
      >
        <el-form
            ref="form"
            :model="ruleForm"
            label-width="100px"
            class="demo-ruleForm"
            size="large"
        >
          <el-form-item label="用户名" prop="username">
            <el-icon>
              <UserFilled/>
            </el-icon>
            <el-input v-model="ruleForm.username" placeholder="输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-icon>
              <Lock/>
            </el-icon>
            <el-input v-model="ruleForm.password" placeholder="输入密码" show-password/>
          </el-form-item>
          <el-form-item label="验证码">
          <div style="display: flex">
            <el-icon><Key/></el-icon>
            <el-input  v-model="ruleForm.validCode" style="width: 120px;" placeholder="请输入验证码"></el-input>
            <ValidCode @input="createValidCode" />
          </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login">登录</el-button>
            <el-button type="info" @click="register">注册</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <el-dialog
          v-model="registerDialogVisible"
          title="Tips"
          width="30%"
      >

        <el-form
            ref="r_form"
            :model="registerForm"
            label-width="100px"
            class="demo-ruleForm"
            size="large"
        >
          <el-form-item label="用户名" prop="username">
            <el-icon>
              <UserFilled/>
            </el-icon>
            <el-input v-model="registerForm.username" placeholder="输入用户名" @blur="judgeUsername"></el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="registerForm.nickName" placeholder="输入昵称"/>
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-icon>
              <Lock/>
            </el-icon>
            <el-input v-model="registerForm.password" placeholder="输入密码" show-password @blur="judgePass"/>
          </el-form-item>
          <el-form-item label="确认密码" prop="check">
            <el-input v-model="check" placeholder="再次输入密码" show-password @blur="judgeSame"/>
          </el-form-item>
          <el-form-item label="验证码">
            <div style="display: flex">
              <el-icon><Key/></el-icon>
              <el-input v-model="registerForm.validCode" style="width: 120px;" placeholder="请输入验证码"></el-input>
              <ValidCode @input="createValidCode" />
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addNewUser">注册</el-button>
            <el-button type="info" @click="back">返回</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import request from "../../util/request";
import {ElMessage} from 'element-plus'
import ValidCode from "@/components/ValidCode";

let flag = false

export default {
  name: "Login",
  components:{
    ValidCode
  },
  methods: {
    createValidCode(data) {
      this.validCode = data
    },
    login() {
      if (!this.ruleForm.validCode) {
        this.$message.error("请填写验证码")
        return
      }
      if(this.ruleForm.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
        this.$message.error("验证码错误")
        return
      }
      request.post("/user/login", this.ruleForm).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "登录成功"
          })
          console.log(res.data)
          window.localStorage.setItem('token', res.data)
          this.$router.push("/home");//跳转
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    back() {
      this.loginDialogVisible = true;
      this.registerDialogVisible = false;
    },
    register() {
      this.loginDialogVisible = false;
      this.registerDialogVisible = true;
    },
    judgePass() {
      let s;
      s = '^[0-9a-zA-Z]{5,11}$';
      if (this.registerForm.password.length < 6 || this.registerForm.password.length > 12) {
        ElMessage.error('密码长度必须为6-12位')
        return false
      } else if (this.registerForm.password.match(s)) {
        return true;
      } else {
        ElMessage({
          showClose: true,
          message: '密码应由数字和字母组成',
          type: 'warning',
        })
        return false
      }

    },
    judgeUsername() {
      request.post('/user/check', this.registerForm).then(res => {
        if (res.code === '0') {
          flag = true;
        } else if (res.code === '110') {
          this.$message({
            type: "error",
            message: res.msg
          })
          flag = false;
        } else flag = false;
      })
      return flag;
    },
    judgeSame() {
      if (this.registerForm.password === this.check) {
        return true
      } else {
        ElMessage.error('前后密码不一致')
        return false
      }
    },
    addNewUser() {
      if (!this.registerForm.validCode) {
        this.$message.error("请填写验证码")
        return
      }
      if(this.registerForm.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
        this.$message.error("验证码错误")
        return
      }
      if (this.judgeSame() && this.judgeUsername() && this.judgePass()) {
        request.post("/user/add", this.registerForm).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "注册成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
        this.back()
      }
    },
  },
  data() {
    return {
      validCode: '',
      ruleForm: {},
      registerForm: {},
      loginDialogVisible: true,
      registerDialogVisible: false,
      check: '',
      bg: require('@/imgs/login_bg.jpg')
    }
  }
}
</script>

<style scoped>

</style>