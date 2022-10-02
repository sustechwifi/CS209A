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
            <el-dropdown-item command="item">Item Class</el-dropdown-item>
            <el-dropdown-item command="city">City</el-dropdown-item>
            <el-dropdown-item command="courier">Courier name</el-dropdown-item>
            <el-dropdown-item command="ship">Ship name</el-dropdown-item>
            <el-dropdown-item command="company">Company name</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button size="default" type="danger">Bulk delete</el-button>
    </el-row>

    <div style="padding-left: 20px">
      <el-button size="default" type="primary" @click="dialogVisible = true">Precise query</el-button>
    </div>

    <el-row style="padding-left: 30px">
      <el-radio-group v-model="searchType">
        <el-radio :label="0">All</el-radio>
        <el-radio :label="1">Not exported</el-radio>
        <el-radio :label="2">Exported</el-radio>
        <el-radio :label="3">Not delivered</el-radio>
        <el-radio :label="4">Finished</el-radio>
      </el-radio-group>
    </el-row>

    <el-dialog v-model="dialogVisible" title="Precise query" width="50%" draggable>

      <el-form :model="form">
        <el-form-item label="Item name">
          <div class="demo-input-suffix">
            <el-row :gutter="20">
              <span>item info</span>
              <el-input v-model="form.itemName" style="width: 200px" placeholder="itemName *"/>
              <el-input v-model="form.itemClass" style="width: 200px" placeholder="itemClass"/>
              <el-input v-model="form.itemPrice" style="width: 200px" placeholder="itemPrice *"/>
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
                placeholder="Pick a date *"
                style="width: 100%"
            />
          </el-col>
        </el-form-item>
        <el-form-item label="Retrieval courier and phone">
          <el-input style="width: 150px" v-model="form.retrievalCourier" placeholder="courier name"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.retrievalCourierPhone" placeholder="phone number *"/>
        </el-form-item>
        <el-form-item label="Retrieval courier gender">
          <el-radio-group v-model="form.retrievalCourierGender">
            <el-radio :label="undefined">null</el-radio>
            <el-radio :label="1">male</el-radio>
            <el-radio :label="2">female</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Delivery city and date">
          <el-input style="width: 150px" v-model="form.deliveryCity" placeholder="retrieval city"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-col :span="11">
            <el-date-picker
                v-model="form.deliveryDate"
                type="date"
                placeholder="Pick a date *"
                style="width: 100%"
            />
          </el-col>
        </el-form-item>
        <el-form-item label="Delivery courier and phone">
          <el-input style="width: 150px" v-model="form.deliveryCourier" placeholder="courier name"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.deliveryCourierPhone" placeholder="phone number *"/>
        </el-form-item>
        <el-form-item label="Delivery courier gender">
          <el-radio-group v-model="form.deliveryCourierGender">
            <el-radio :label="undefined">null</el-radio>
            <el-radio :label="1">male</el-radio>
            <el-radio :label="2">female</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Item export">
          <el-row>
            <el-input style="width: 20%" v-model="form.exportCity" placeholder="export city"/>
            <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
            <el-col :span="11">
              <el-date-picker
                  v-model="form.exportDate"
                  type="date"
                  placeholder="Pick a date *"
                  style="width: 100%"
              />
            </el-col>
            <el-input style="width: 100px" v-model="form.exportTax" placeholder="export tax *"/>
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
                  placeholder="Pick a date *"
                  style="width: 100%"
              />
            </el-col>
            <el-input style="width: 100px" v-model="form.importTax" placeholder="import tax *"/>
          </el-row>
        </el-form-item>

        <el-form-item label="Container">
          <el-input style="width: 200px" v-model="form.containerCode" placeholder="container code *"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.containerType" placeholder="container type"/>
        </el-form-item>

        <el-form-item label="Company and ship">
          <el-input style="width: 200px" v-model="form.company" placeholder="company"/>
          <span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</span>
          <el-input style="width: 200px" v-model="form.ship" placeholder="ship"/>
        </el-form-item>

      </el-form>


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
          <el-input v-model="scope.row.retrievalDate"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Retrieval Courier">
        <template v-slot="scope">
          <el-input v-model="scope.row.retrievalCourier"></el-input>
          <el-input v-model="scope.row.retrievalCourierPhone"></el-input>
          <el-input v-model="scope.row.retrievalCourierGender"></el-input>
          <el-input v-model="scope.row.retrievalCourierAge"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Export">
        <template v-slot="scope">
          <el-input v-model="scope.row.exportCity"></el-input>
          <el-input v-model="scope.row.exportDate"></el-input>
          <el-input v-model="scope.row.exportTax"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Import">
        <template v-slot="scope">
          <el-input v-model="scope.row.importCity"></el-input>
          <el-input v-model="scope.row.importDate"></el-input>
          <el-input v-model="scope.row.importTax"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Delivery">
        <template v-slot="scope">
          <el-input v-model="scope.row.deliveryCity"></el-input>
          <el-input v-model="scope.row.deliveryDate"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Delivery Courier">
        <template v-slot="scope">
          <el-input v-model="scope.row.deliveryCourier"></el-input>
          <el-input v-model="scope.row.deliveryCourierPhone"></el-input>
          <el-input v-model="scope.row.deliveryCourierGender"></el-input>
          <el-input v-model="scope.row.deliveryCourierAge"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Company">
        <template v-slot="scope">
          <el-input v-model="scope.row.company"></el-input>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="Container">
        <template v-slot="scope">
          <el-input v-model="scope.row.containerCode"></el-input>
          <el-input v-model="scope.row.containerType"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="log time">
        <template v-slot="scope">
          <el-input v-model="scope.row.logTime"></el-input>
        </template>
      </el-table-column>

      <el-table-column label="Operations">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
          >Edit
          </el-button
          >
          <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
          >Delete
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <hr>
    <div class="demo-pagination-block">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 15, 25, 35]"
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
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "查询完毕，" + res.msg
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleCommand(command) {
      this.searchContent = command;
      ElMessage(`查询 ${command}`)
    },
    handlePreciseSearch() {
      console.log(this.form);
      request.post("search/precise/" + this.searchType +
          "/" + this.pageSize +
          "/" + this.currentPage
          , this.form).then(res => {
        this.tableData = res.rows;
        this.totalRows = res.totalCount-1;
        console.log(res.rows)
        this.$message({
          type: "success",
          message: "查询完毕"
        })
      });
    },
    handleSelectionChange() {

    },
    handleEdit() {

    },
    handleDelete() {

    },
    showContent(row) {

    },
    handleSizeChange() {
      this.handlePreciseSearch();
    },
    handleCurrentChange() {
      this.handlePreciseSearch();
    }
  },
  data() {
    return {
      search: '',
      searchContent: '',
      searchType: 0,
      tableData: [],
      currentPage: 1,
      pageSize: 15,
      totalRows: 0,
      dialogVisible: false,
      form: {
        itemName: '',
        itemClass: '',
        itemPrice: '',
        ship: '',
        company: '',
        retrievalCity: '',
        retrievalCourier: '',
        retrievalCourierPhone: '',
        retrievalCourierGender:undefined,
        deliveryCity: '',
        deliveryCourier: '',
        deliveryCourierPhone: '',
        deliveryCourierGender:undefined,
        containerCode: '',
        containerType: '',
        exportTax: undefined,
        exportCity: '',
        importTax: undefined,
        importCity: '',
      },
    }
  }
}
</script>

<style scoped>

</style>