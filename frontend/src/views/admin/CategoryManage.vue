<template>
  <div>
    <h2>分类管理 <el-button type="primary" size="small" @click="showDialog()">新增</el-button></h2>
    <el-table :data="categories">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="sortOrder" label="排序" />
      <el-table-column label="操作" width="150">
        <template #default="{row}">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteCategory(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="form.id?'编辑分类':'新增分类'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveCategory">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const categories = ref([])
const dialogVisible = ref(false)
const form = ref({})

const loadCategories = async () => { categories.value = (await api.getCategories()).data }

const showDialog = (row) => {
  form.value = row ? { ...row } : { sortOrder: 0 }
  dialogVisible.value = true
}

const saveCategory = async () => {
  if (form.value.id) await api.updateCategory(form.value.id, form.value)
  else await api.createCategory(form.value)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadCategories()
}

const deleteCategory = async (id) => {
  try {
    await api.deleteCategory(id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch {}
}

onMounted(loadCategories)
</script>
