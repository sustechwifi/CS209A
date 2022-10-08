<template>
  <div class="common-layout" style="padding: 100px">
    <el-container>
      <el-header style="margin: auto">
        <h1>Advance search Test</h1>
      </el-header>

      <el-divider/>
      <div class="demo-collapse" style="height: 80%">
        <el-collapse v-model="activeNames">
          <el-collapse-item title="task1" name="1">
            <div>
              <h3>find the oldest container</h3>
              <hr>
              <el-row>
                <el-input style="width: 300px;" v-model="container.code"
                          placeholder="input the code of container"></el-input>
                <span>--</span>
                <el-input style="width: 300px;" v-model="container.type"
                          placeholder="*input the type of container"></el-input>
                <div style="margin : auto">
                  <el-button size="default" type="primary" @click="task1">search</el-button>
                </div>
              </el-row>
            </div>
          </el-collapse-item>
          <el-collapse-item title="task2" name="2">
            <div>
              <h3>find the most hard-working courier by company and city</h3>
              <hr>
            </div>
            <el-row>
              <el-input style="width: 300px;padding-right: 30px" v-model="city"
                        placeholder="input the city"></el-input>
              <el-input style="width: 300px;padding-right: 30px" v-model="company"
                        placeholder="input the company"></el-input>
              <el-button size="default" type="primary" @click="task2">search</el-button>
            </el-row>
          </el-collapse-item>
          <el-collapse-item title="task3" name="3">
            <div>
              <h3>find the city that has the least cost of export</h3>
              <hr>
            </div>
            <div>
              <el-button size="default" type="primary" @click="task3">search by random</el-button>
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
  name: "Advance",
  methods:{
    task1(){
      request.post("/advance/task1",this.container).then(res=>{
        if (res.code === '0'){
          let data = JSON.stringify(res.data);
          data = data.substring(1,data.length-1) + ' days';
          this.$message({
            type:'success',
            message:res.msg+"---"+data
          })
        }
        console.log(res);
      })
    },
    task2(){
      request.post("/advance/task2/"+this.city+"/"+this.company).then(res=>{
        if (res.code === '0'){
          let courier = res.data.concat;
          let num = res.data.num;
          let data = 'courier:('+courier+") count:"+num;
          this.$message({
            type:'success',
            message:(res.msg+"---"+data)
          })
        }
        console.log(res);
      })
    },
    task3(){
      request.get("/advance/task3/").then(res=>{
        if (res.code === '0'){
          let cost = res.data.cost;
          let city = res.data.name;
          let data = 'city:'+city+" total cost:"+cost;
          this.$message({
            type:'success',
            message:res.msg+"---"+data
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
    }
  }
}
</script>

<style scoped>

</style>