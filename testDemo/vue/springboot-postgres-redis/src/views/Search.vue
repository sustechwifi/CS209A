<template>
  <div class="demo-input-suffix">
    <el-row :gutter="20" style="padding: 20px">
      <el-input
          style="width: 30%;padding-right: 20px"
          v-model="search"
          size="default"
          placeholder="Type something"
      />
      <el-dropdown style="padding-right: 20px" size="default" split-button type="primary" @click="handleClick">
        Batch query
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="item">Item Type</el-dropdown-item>
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

    <el-dialog v-model="dialogVisible" title="Precise query" width="50%" draggable>

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
        <el-form-item label="Retrieval courier gender">
          <el-switch v-model="form.retrievalCourierGender"/>
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
        <el-form-item label="Delivery courier gender">
          <el-switch v-model="form.deliveryCourierGender"/>
        </el-form-item>

        <el-form-item label="Item export">
          <el-row>
            <el-input style="width: 100px" v-model="form.exportCity" placeholder="export city"/>
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
            <el-input style="width: 100px" v-model="form.importCity" placeholder="import city"/>
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

        <el-form-item label="log time">
          <el-date-picker
              v-model="form.logTime"
              type="date"
              placeholder="Pick a date"
              style="width: 200px"
          />
        </el-form-item>
      </el-form>


      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="dialogVisible = false"
        >Confirm</el-button>
      </span>
      </template>
    </el-dialog>

    <el-table
        ref="multipleTableRef"
        :data="tableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"/>
      <el-table-column prop="content" label="item">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Retrieval">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Retrieval Courier">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Export">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Import">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Delivery">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Delivery Courier">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="content" label="Company">
        <template v-slot="scope">
          <el-button type="primary" @click="showContent(scope.row.content)">item</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="Container"/>
      <el-table-column prop="name" label="log time"/>

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
  </div>
</template>

<script>

export default {
  name: "Search",
  methods: {
    handleClick() {

    },
    handleCommand(command) {

    },
    handleSelectionChange() {

    },
    handleEdit() {

    },
    handleDelete() {

    },
    showContent() {

    }
  },
  data() {
    return {
      search: '',
      tableData: [],
      dialogVisible: false,
      form: {},
    }
  }
}
</script>

<style scoped>

</style>