<template>
  <div>
    <h2>订单管理</h2>
    <el-table :data="orders" border stripe>
      <el-table-column prop="orderNo" label="订单号" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column label="状态">
        <template #default="{row}">{{ statusMap[row.status] }}</template>
      </el-table-column>
      <el-table-column prop="receiverName" label="收货人" />
      <el-table-column prop="receiverPhone" label="电话" />
      <el-table-column prop="createTime" label="下单时间" />
      <el-table-column label="操作" width="120">
        <template #default="{row}">
          <el-button size="small" type="primary" v-if="row.status===1" @click="shipOrder(row.id)">发货</el-button>
          <span v-else-if="row.status===0">待付款</span>
          <span v-else-if="row.status===2">已发货</span>
          <span v-else-if="row.status===3">已完成</span>
          <span v-else-if="row.status===4">已取消</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination 
        v-model:current-page="page" 
        :page-size="size" 
        :total="total" 
        @current-change="loadOrders" 
        layout="total, prev, pager, next, jumper"
        background />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const orders = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const statusMap = { 0: '待付款', 1: '已付款', 2: '已发货', 3: '已完成', 4: '已取消' }

const loadOrders = async () => {
  const res = await api.getAllOrders({ page: page.value, size: size.value })
  orders.value = res.data.list
  total.value = res.data.total
}

const shipOrder = async (id) => {
  await api.updateOrderStatus(id, { status: 2 })
  ElMessage.success('发货成功')
  loadOrders()
}

onMounted(loadOrders)
</script>

<style scoped>
.pagination-container { margin-top: 20px; display: flex; justify-content: center; }
</style>
