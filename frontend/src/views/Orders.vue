<template>
  <div>
    <h2>我的订单</h2>
    <el-table :data="orders" border stripe>
      <el-table-column prop="orderNo" label="订单号" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column label="状态">
        <template #default="{row}">{{ statusMap[row.status] }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" />
      <el-table-column label="操作" width="280">
        <template #default="{row}">
          <el-button size="small" @click="$router.push(`/order/${row.id}`)">详情</el-button>
          <el-button size="small" type="primary" v-if="row.status===0" @click="payOrder(row.id)">付款</el-button>
          <el-button size="small" type="danger" v-if="row.status===0" @click="cancelOrder(row.id)">取消</el-button>
          <el-button size="small" type="success" v-if="row.status===2" @click="confirmReceive(row.id)">确认收货</el-button>
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
  const res = await api.getOrders({ page: page.value, size: size.value })
  orders.value = res.data.list
  total.value = res.data.total
}

const payOrder = async (id) => {
  await api.updateOrderStatus(id, { status: 1 })
  ElMessage.success('付款成功')
  loadOrders()
}

const cancelOrder = async (id) => {
  await api.cancelOrder(id)
  ElMessage.success('订单已取消')
  loadOrders()
}

const confirmReceive = async (id) => {
  await api.updateOrderStatus(id, { status: 3 })
  ElMessage.success('已确认收货')
  loadOrders()
}

onMounted(loadOrders)
</script>

<style scoped>
.pagination-container { margin-top: 20px; display: flex; justify-content: center; }
</style>
