<template>
  <div class="demo-input-suffix">
    <el-row :gutter="20" style="padding: 20px">
      <el-input
          style="width: 30%;padding-right: 20px"
          v-model="search"
          size="default"
          placeholder="Type something"
      />
      <el-dropdown style="padding-right: 20px" size="default" split-button type="primary" @click="handleClick"
                   @command="handleCommand">
        Batch query
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="null">null</el-dropdown-item>
            <el-dropdown-item command="itemClass">Item Class</el-dropdown-item>
            <el-dropdown-item command="ship">Ship name</el-dropdown-item>
            <el-dropdown-item command="company">Company name</el-dropdown-item>
            <el-dropdown-item command="container">Container type</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-dropdown style="padding-right: 20px" size="default" split-button type="warning" @click="updateAll"
                   @command="handleUpdateType">
        Bulk updates
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="1">update not exported</el-dropdown-item>
            <el-dropdown-item command="2">update exported</el-dropdown-item>
            <el-dropdown-item command="3">update not delivery</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button size="default" type="danger" @click="deleteByIds">Bulk delete</el-button>
    </el-row>

    <el-row>
      <el-switch
          v-model="precise"
          size="default"
          active-text="use precise search"
          style="padding-left: 20px"
      />
      <div style="padding-left: 20px" :hidden="!precise">
        <el-button size="default" type="primary" @click="dialogVisible = true">Precise query</el-button>
      </div>
    </el-row>
    <el-row style="padding-left: 30px">
      <el-radio-group v-model="searchType" @change="this.route();">
        <el-radio :disabled="!precise" :label="0">All</el-radio>
        <el-radio :label="1">Not exported</el-radio>
        <el-radio :label="2">Exported</el-radio>
        <el-radio :label="3">Not delivered</el-radio>
        <el-radio :label="4">Finished</el-radio>
      </el-radio-group>
    </el-row>

    <el-dialog v-model="dialogVisible" :title="this.updateType ? 'Update':'Precise query'" width="50%" draggable>

      <el-form :model="form">
        <el-form-item label="Item name">
          <div class="demo-input-suffix">
            <el-row :gutter="20">
              <span>item info</span>
              <el-input v-model="form.itemName" style="width: 200px" placeholder="itemName"/>
              <el-input v-model="form.itemClass" style="width: 200px" placeholder="itemClass"/>
              <el-input v-model="form.itemPrice" style="width: 200px" placeholder="itemPrice"/>
            </el-row>
          </div>
        </el-form-item>
        <el-form-item label="Retrieval city and date">
          <el-input style="width: 150px" v-model="form.retrievalCity" placeholder="retrieval city"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-col :span="11">
            <el-date-picker
                v-model="form.retrievalDate"
                type="date"
                placeholder="Pick a date"
                style="width: 100%"
            />
          </el-col>
        </el-form-item>
        <el-form-item label="Retrieval courier and phone">
          <el-input style="width: 150px" v-model="form.retrievalCourier" placeholder="courier name"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.retrievalCourierPhone" placeholder="phone number"/>
        </el-form-item>
        <el-form-item label="Retrieval courier gender and birth year">
          <el-radio-group v-model="form.retrievalCourierGender">
            <el-radio :label="undefined">null</el-radio>
            <el-radio :label="1">male</el-radio>
            <el-radio :label="0">female</el-radio>
          </el-radio-group>
          <el-input style="width: 150px;padding-left: 50px" v-model="form.retrievalCourierAge" placeholder="birth year"/>
        </el-form-item>
        <el-form-item label="Delivery city and date">
          <el-input style="width: 150px" v-model="form.deliveryCity" placeholder="retrieval city"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-col :span="11">
            <el-date-picker
                v-model="form.deliveryDate"
                type="date"
                placeholder="Pick a date"
                style="width: 100%"
            />
          </el-col>
        </el-form-item>
        <el-form-item label="Delivery courier and phone">
          <el-input style="width: 150px" v-model="form.deliveryCourier" placeholder="courier name"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.deliveryCourierPhone" placeholder="phone number"/>
        </el-form-item>
        <el-form-item label="Delivery courier gender and birth year">
          <el-radio-group v-model="form.deliveryCourierGender">
            <el-radio :label="undefined">null</el-radio>
            <el-radio :label="1">male</el-radio>
            <el-radio :label="0">female</el-radio>
          </el-radio-group>
          <el-input style="width: 150px;padding-left: 50px" v-model="form.deliveryCourierAge" placeholder="birth year"/>
        </el-form-item>

        <el-form-item label="Item export">
          <el-row>
            <el-input style="width: 20%" v-model="form.exportCity" placeholder="export city"/>
            <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
            <el-col :span="11">
              <el-date-picker
                  v-model="form.exportDate"
                  type="date"
                  placeholder="Pick a date"
                  style="width: 100%"
              />
            </el-col>
            <el-input style="width: 100px" v-model="form.exportTax" placeholder="export tax"/>
          </el-row>
        </el-form-item>

        <el-form-item label="Item import">
          <el-row>
            <el-input style="width: 20%" v-model="form.importCity" placeholder="import city"/>
            <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
            <el-col :span="11">
              <el-date-picker
                  v-model="form.importDate"
                  type="date"
                  placeholder="Pick a date"
                  style="width: 100%"
              />
            </el-col>
            <el-input style="width: 100px" v-model="form.importTax" placeholder="import tax"/>
          </el-row>
        </el-form-item>

        <el-form-item label="Container">
          <el-input style="width: 200px" v-model="form.containerCode" placeholder="container code"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.containerType" placeholder="container type"/>
        </el-form-item>

        <el-form-item label="Company and ship">
          <el-input style="width: 200px" v-model="form.company" placeholder="company"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.ship" placeholder="ship"/>
        </el-form-item>

      </el-form>

      <el-alert title="It will be slow if only general columns with are filled" type="warning"/>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handlePreciseSearch">Confirm</el-button>
      </span>
      </template>
    </el-dialog>
    <hr>
    <el-table
        ref="multipleTableRef"
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"/>
      <el-table-column prop="content" label="item">
        <template v-slot="scope">
          <el-input v-model="scope.row.itemName"></el-input>
          <el-input v-model="scope.row.itemClass"></el-input>
          <el-input v-model="scope.row.itemPrice"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Retrieval">
        <template v-slot="scope">
          <el-input v-model="scope.row.retrievalCity"></el-input>
          <Span>{{ getDate(scope.row.retrievalDate) }}</Span>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Retrieval Courier">
        <template v-slot="scope">
          <el-input v-model="scope.row.retrievalCourier"></el-input>
          <el-input v-model="scope.row.retrievalCourierPhone"></el-input>
          <span>{{scope.row.retrievalCourierGender?'男':'女'}}</span>
          <el-input v-model="scope.row.retrievalCourierAge"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Export">
        <template v-slot="scope">
          <el-input v-model="scope.row.exportCity"></el-input>
          <Span>{{ getDate(scope.row.exportDate) }}</Span>
          <el-input v-model="scope.row.exportTax"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Import">
        <template v-slot="scope">
          <el-input v-model="scope.row.importCity"></el-input>
          <Span>{{ getDate(scope.row.importDate) }}</Span>
          <el-input v-model="scope.row.importTax"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Delivery">
        <template v-slot="scope">
          <el-input v-model="scope.row.deliveryCity"></el-input>
          <Span>{{ getDate(scope.row.deliveryDate) }}</Span>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Delivery Courier">
        <template v-slot="scope">
          <el-input v-model="scope.row.deliveryCourier"></el-input>
          <el-input v-model="scope.row.deliveryCourierPhone"></el-input>
          <span>{{scope.row.deliveryCourierGender?'男':'女'}}</span>
          <el-input v-model="scope.row.deliveryCourierAge"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Company">
        <template v-slot="scope">
          <el-input v-model="scope.row.ship"></el-input>
          <el-input v-model="scope.row.company"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="Container">
        <template v-slot="scope">
          <el-input v-model="scope.row.containerCode"></el-input>
          <el-input v-model="scope.row.containerType"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="log time" width="150px">
        <template v-slot="scope">
          <Span>{{ getTime(scope.row.logTime) }}</Span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="Operations" width="100px">-->
      <!--        <template #default="scope">-->
      <!--          <el-button @click="handleEdit(scope.$index, scope.row)">Edit</el-button>-->
      <!--        </template>-->
      <!--      </el-table-column>-->

    </el-table>
    <hr>
    <div class="demo-pagination-block">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[1, 5, 15, 25]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalRows"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>

import request from "../../util/request";
import {ElMessage} from "element-plus";

export default {
  name: "Search",
  methods: {
    handleClick() {
      request.get("search/batch", {
        params: {
          item: this.searchContent,
          type: this.searchType,
          search: this.search,
          begin: this.currentPage,
          pageSize: this.pageSize
        }
      }).then(res => {
        this.rowIds = res.ids;
        this.tableData = res.rows;
        this.totalRows = res.totalCount;
        this.$message({
          type: "success",
          message: "查询完毕"
        })
      })
    },
    handleCommand(command) {
      this.searchContent = command;
      ElMessage(`查询 ${command}`)
    },
    handlePreciseSearch() {
      console.log(this.form);
      if (this.form.recordId) {
        request.post("/dml/update", this.form).then(res => {
          this.$message({
            type: "success",
            message: "更新完毕，" + res.msg
          })
          this.form = [];
          this.dialogVisible = false;
        })
      } else if (this.updateType) {
        request.post("/dml/updateBatch/" + this.updateType, this.form).then(res => {
          this.$message({
            type: "success",
            message: "更新完毕，" + res.msg
          })
          this.dialogVisible = false;
          this.updateType = 0;
        })
      } else {
        request.post("search/precise/" + this.searchType +
            "/" + this.pageSize +
            "/" + this.currentPage
            , this.form).then(res => {
          this.tableData = res.rows;
          this.totalRows = res.totalCount;
          console.log(res.rows)
          this.$message({
            type: "success",
            message: "查询完毕"
          })
          this.dialogVisible = false;
        });
      }
    },
    handleUpdateType(command) {
      this.updateType = command;
      ElMessage(`更新 ${command}`)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleEdit(index, row) {
      this.form = row;
      this.form.logTime = Date.now();
      this.dialogVisible = true;
    },
    updateAll() {
      this.dialogVisible = true;
    },
    route() {
      if (this.precise) {
        this.handlePreciseSearch();
      } else {
        this.handleClick();
      }
    },
    deleteByIds() {
      let ids = [];
      for (let i = 0; i < this.multipleSelection.length; i++) {
        ids[i] = this.multipleSelection[i].recordId;
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
      })
    },
    getDate(value) {//调用时间戳为日期显示
      if (!value) return '未进行';
      let date = new Date(value)
      let y = date.getFullYear()  //获取年份
      let m = date.getMonth() + 1  //获取月份
      m = m < 10 ? "0" + m : m  //月份不满10天显示前加0
      let d = date.getDate()  //获取日期
      d = d < 10 ? "0" + d : d  //日期不满10天显示前加0
      return y + "-" + m + "-" + d
    },
    getTime(value) {//调用时间戳为日期显示
      if (!value) return '';
      let date = new Date(value)
      let y = date.getFullYear()  //获取年份
      let m = date.getMonth() + 1  //获取月份
      m = m < 10 ? "0" + m : m  //月份不满10天显示前加0
      let d = date.getDate()  //获取日期
      d = d < 10 ? "0" + d : d  //日期不满10天显示前加0
      let h = date.getHours();                   //小时
      h = h < 10 ? "0" + h : h  //日期不满10天显示前加0
      let mm = date.getMinutes();                 //分
      mm = mm < 10 ? "0" + mm : mm  //日期不满10天显示前加0
      let s = date.getSeconds();                 //秒
      s = s < 10 ? "0" + s : s  //日期不满10天显示前加0
      return y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + s
    },
    handleSizeChange() {
      this.route();
    },
    handleCurrentChange() {
      this.route();
    },
  },
  data() {
    return {
      search: '',
      precise: false,
      searchContent: '',
      searchType: 4,
      tableData: [],
      currentPage: 1,
      pageSize: 15,
      totalRows: 0,
      dialogVisible: false,
      updateType: 0,
      rowIds: [],
      multipleSelection: [],
      form: {
        itemName: '',
        itemClass: '',
        itemPrice: '',
        ship: '',
        company: '',
        retrievalCity: '',
        retrievalCourier: '',
        retrievalCourierPhone: '',
        retrievalCourierGender: undefined,
        deliveryCity: '',
        deliveryCourier: '',
        deliveryCourierPhone: '',
        deliveryCourierGender: undefined,
        containerCode: '',
        containerType: '',
        exportTax: undefined,
        exportCity: '',
        importTax: undefined,
        importCity: '',
        logTime: '',
      },
    }
  },
}
</script>

<style scoped>

</style>