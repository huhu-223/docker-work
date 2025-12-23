<template>
  <div>
    <div class="search-bar">
      <el-select v-model="categoryId" placeholder="选择分类" clearable style="width:150px">
        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <el-input v-model="keyword" placeholder="搜索商品" style="width:200px;margin:0 10px" />
      <el-button type="primary" @click="loadProducts">搜索</el-button>
    </div>
    <el-row :gutter="20">
      <el-col :span="6" v-for="p in products" :key="p.id">
        <el-card class="product-card" @click="$router.push(`/product/${p.id}`)">
          <div class="product-name">{{ p.name }}</div>
          <div class="product-price">¥{{ p.price }}</div>
          <div class="product-category">{{ p.categoryName }}</div>
        </el-card>
      </el-col>
    </el-row>
    <div class="pagination-container">
      <el-pagination 
        v-model:current-page="page" 
        :page-size="size" 
        :total="total" 
        @current-change="loadProducts" 
        layout="total, prev, pager, next, jumper"
        background />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const products = ref([])
const categories = ref([])
const categoryId = ref(null)
const keyword = ref('')
const page = ref(1)
const size = ref(8)
const total = ref(0)

const loadProducts = async () => {
  const res = await api.getProducts({ page: page.value, size: size.value, categoryId: categoryId.value, keyword: keyword.value })
  products.value = res.data.list
  total.value = res.data.total
}

const loadCategories = async () => {
  const res = await api.getCategories()
  categories.value = res.data
}

onMounted(() => { loadProducts(); loadCategories() })
</script>

<style scoped>
.search-bar { margin-bottom: 20px; }
.product-card { cursor: pointer; margin-bottom: 20px; }
.product-name { font-size: 16px; font-weight: bold; }
.product-price { color: #f56c6c; font-size: 18px; margin: 10px 0; }
.product-category { color: #999; font-size: 12px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: center; }
</style>
