<template>
  <div class="common-layout">
    <el-container>
      <el-header><hr></el-header>
      <el-main>
        <el-upload
            class="upload-demo"
            drag
            action="http://localhost:9090/file/upload/"
            multiple
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            Drop file here or <em>click to upload</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              csv files with a size less than 500kb
            </div>
          </template>
        </el-upload>
      </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>

</template>

<script>
export default {
  name: "Add"
}
</script>

<style scoped>

</style>