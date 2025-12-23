<template>
  <div>
    <h2>商品管理 <el-button type="primary" size="small" @click="showDialog()">新增</el-button></h2>
    <div class="search-bar">
      <el-select v-model="categoryId" placeholder="选择分类" clearable style="width:150px">
        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <el-input v-model="keyword" placeholder="搜索商品名称" style="width:200px;margin:0 10px" />
      <el-button type="primary" @click="loadProducts">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>
    <el-table :data="products" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="stock" label="库存" />
      <el-table-column prop="categoryName" label="分类" />
      <el-table-column label="状态">
        <template #default="{row}">{{ row.status===1?'上架':'下架' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{row}">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteProduct(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination 
        v-model:current-page="page" 
        :page-size="size" 
        :total="total" 
        @current-change="loadProducts" 
        layout="total, prev, pager, next, jumper"
        background />
    </div>
    <el-dialog v-model="dialogVisible" :title="form.id?'编辑商品':'新增商品'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :precision="2" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="上架" :value="1" /><el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const products = ref([])
const categories = ref([])
const categoryId = ref(null)
const keyword = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const form = ref({})

const loadProducts = async () => {
  const res = await api.getAllProducts({ page: page.value, size: size.value, categoryId: categoryId.value, keyword: keyword.value })
  products.value = res.data.list
  total.value = res.data.total
}

const loadCategories = async () => { categories.value = (await api.getCategories()).data }

const resetSearch = () => {
  categoryId.value = null
  keyword.value = ''
  page.value = 1
  loadProducts()
}

const showDialog = (row) => {
  form.value = row ? { ...row } : { status: 1 }
  dialogVisible.value = true
}

const saveProduct = async () => {
  if (form.value.id) await api.updateProduct(form.value.id, form.value)
  else await api.createProduct(form.value)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadProducts()
}

const deleteProduct = async (id) => {
  await api.deleteProduct(id)
  ElMessage.success('删除成功')
  loadProducts()
}

onMounted(() => { loadProducts(); loadCategories() })
</script>

<style scoped>
.search-bar { margin-bottom: 20px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: center; }
</style>
