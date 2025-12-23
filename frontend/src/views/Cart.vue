<template>
  <div>
    <h2>购物车</h2>
    <el-table :data="cart.items" v-if="cart.items?.length">
      <el-table-column prop="productName" label="商品" />
      <el-table-column prop="productPrice" label="单价" />
      <el-table-column label="数量">
        <template #default="{row}">
          <el-input-number v-model="row.quantity" :min="1" size="small" @change="updateQuantity(row)" />
        </template>
      </el-table-column>
      <el-table-column label="小计">
        <template #default="{row}">¥{{ (row.productPrice * row.quantity).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{row}">
          <el-button type="danger" size="small" @click="removeItem(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-else description="购物车为空" />
    <div v-if="cart.items?.length" style="margin-top:20px;text-align:right">
      <span style="font-size:18px">总计：<b style="color:#f56c6c">¥{{ cart.total }}</b></span>
      <el-button type="primary" @click="showCheckout=true" style="margin-left:20px">去结算</el-button>
    </div>
    <el-dialog v-model="showCheckout" title="选择收货地址">
      <el-radio-group v-model="selectedAddress">
        <el-radio v-for="a in addresses" :key="a.id" :value="a.id" style="display:block;margin:10px 0">
          {{ a.receiverName }} {{ a.phone }} {{ a.province }}{{ a.city }}{{ a.district }}{{ a.detailAddress }}
        </el-radio>
      </el-radio-group>
      <template #footer>
        <el-button @click="showCheckout=false">取消</el-button>
        <el-button type="primary" @click="createOrder">确认下单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '@/api'

const router = useRouter()
const cart = ref({})
const addresses = ref([])
const showCheckout = ref(false)
const selectedAddress = ref(null)

const loadCart = async () => { cart.value = (await api.getCart()).data }
const loadAddresses = async () => { addresses.value = (await api.getAddresses()).data }

const updateQuantity = async (row) => {
  await api.updateCartItem(row.id, { quantity: row.quantity })
  loadCart()
}

const removeItem = async (id) => {
  await api.removeCartItem(id)
  loadCart()
}

const createOrder = async () => {
  if (!selectedAddress.value) { ElMessage.warning('请选择地址'); return }
  try {
    await api.createOrder({ addressId: selectedAddress.value })
    ElMessage.success('下单成功')
    showCheckout.value = false
    router.push('/orders')
  } catch (err) {
    // 错误已在API拦截器中处理，这里不需要额外处理
  }
}

onMounted(() => { loadCart(); loadAddresses() })
</script>
