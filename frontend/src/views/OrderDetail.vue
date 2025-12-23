<template>
  <div v-if="order">
    <el-card>
      <h2>订单详情</h2>
      <p>订单号：{{ order.orderNo }}</p>
      <p>状态：{{ statusMap[order.status] }}</p>
      <p>收货人：{{ order.receiverName }} {{ order.receiverPhone }}</p>
      <p>地址：{{ order.receiverAddress }}</p>
      <p>下单时间：{{ order.createTime }}</p>
      <el-table :data="order.items" style="margin-top:20px">
        <el-table-column prop="productName" label="商品" />
        <el-table-column prop="productPrice" label="单价" />
        <el-table-column prop="quantity" label="数量" />
        <el-table-column prop="subtotal" label="小计" />
      </el-table>
      <p style="text-align:right;font-size:18px;margin-top:20px">总计：<b style="color:#f56c6c">¥{{ order.totalAmount }}</b></p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api'

const route = useRoute()
const order = ref(null)
const statusMap = { 0: '待付款', 1: '已付款', 2: '已发货', 3: '已完成', 4: '已取消' }

onMounted(async () => {
  const res = await api.getOrder(route.params.id)
  order.value = res.data
})
</script>
