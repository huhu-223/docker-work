<template>
  <div>
    <h2>收货地址 <el-button type="primary" size="small" @click="showDialog()">新增</el-button></h2>
    <el-table :data="addresses">
      <el-table-column prop="receiverName" label="收货人" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column label="地址">
        <template #default="{row}">{{ row.province }}{{ row.city }}{{ row.district }}{{ row.detailAddress }}</template>
      </el-table-column>
      <el-table-column label="默认">
        <template #default="{row}">
          <el-tag v-if="row.isDefault" type="success">默认</el-tag>
          <el-button v-else size="small" @click="setDefault(row.id)">设为默认</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{row}">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteAddress(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="form.id?'编辑地址':'新增地址'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="收货人"><el-input v-model="form.receiverName" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="省份"><el-input v-model="form.province" /></el-form-item>
        <el-form-item label="城市"><el-input v-model="form.city" /></el-form-item>
        <el-form-item label="区县"><el-input v-model="form.district" /></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="form.detailAddress" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const addresses = ref([])
const dialogVisible = ref(false)
const form = ref({})

const loadAddresses = async () => { addresses.value = (await api.getAddresses()).data }

const showDialog = (row) => {
  form.value = row ? { ...row } : {}
  dialogVisible.value = true
}

const saveAddress = async () => {
  if (form.value.id) await api.updateAddress(form.value.id, form.value)
  else await api.createAddress(form.value)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadAddresses()
}

const deleteAddress = async (id) => {
  await api.deleteAddress(id)
  ElMessage.success('删除成功')
  loadAddresses()
}

const setDefault = async (id) => {
  await api.setDefaultAddress(id)
  loadAddresses()
}

onMounted(loadAddresses)
</script>
