<template>
  <div v-if="product">
    <el-card>
      <h2>{{ product.name }}</h2>
      <p class="price">¥{{ product.price }}</p>
      <p>分类：{{ product.categoryName }}</p>
      <p>库存：{{ product.stock }}</p>
      <p>{{ product.description }}</p>
      <div style="margin-top:20px">
        <el-input-number v-model="quantity" :min="1" :max="product.stock" />
        <el-button type="primary" @click="addToCart" style="margin-left:10px">加入购物车</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '@/api'

const route = useRoute()
const product = ref(null)
const quantity = ref(1)

onMounted(async () => {
  const res = await api.getProduct(route.params.id)
  product.value = res.data
})

const addToCart = async () => {
  await api.addToCart({ productId: product.value.id, quantity: quantity.value })
  ElMessage.success('已加入购物车')
}
</script>

<style scoped>
.price { color: #f56c6c; font-size: 24px; }
</style>
