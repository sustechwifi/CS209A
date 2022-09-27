<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <hr>
        <div style="margin: 20px">
          <el-button type="danger" size="default" @click="deleteAll">清除所有数据</el-button>
        </div>
      </el-header>
      <el-main>
        <el-upload
            class="upload-demo"
            drag
            action="http://localhost:9090/file/upload/"
            multiple
        >
          <el-icon class="el-icon--upload">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">
            拖拽文件到此处 或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              csv files with a size less than 500kb
            </div>
          </template>
        </el-upload>
      </el-main>
      <el-footer>
        <el-row>
          <el-switch
              v-model="local"
              size="large"
              active-text="使用本地文件"
              style="padding-right: 20px"
          />
          <el-upload ref="uploadRef" class="upload-demo" action="" :auto-upload="false">
            <template #trigger>
              <el-button type="primary" size="default">浏览文件</el-button>
            </template>
          </el-upload>
          <el-input style="width: 50%;padding-right: 50px;padding-left: 50px" v-model="path" :disabled="!local" size="default"
                    placeholder="Please input path"/>
          <el-button type="primary" size="default" @click="uploadLocal">确认</el-button>
        </el-row>
      </el-footer>
    </el-container>
  </div>

</template>

<script>
import request from "../../util/request";

export default {
  name: "Add",
  methods: {
    deleteAll() {
      request.delete("/file/deleteAll").then(res => {
        //console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功,"+res.data
          })
        }
      })
    },
    uploadLocal(){
      request.post("/file/uploadLocal",this.path).then(res =>{
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "上传成功"+res.data
          })
        }
        else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  },
  data() {
    return {
      local: false,
      path: '',
    }
  }
}
</script>

<style scoped>

</style>