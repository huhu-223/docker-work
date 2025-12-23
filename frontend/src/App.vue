<template>
  <el-container>
    <el-header>
      <div class="header-content">
        <span class="logo" @click="$router.push('/')">电商系统</span>
        <el-menu mode="horizontal" :ellipsis="false" router>
          <el-menu-item index="/products">商品</el-menu-item>
          <el-menu-item index="/cart" v-if="user">购物车</el-menu-item>
          <el-menu-item index="/orders" v-if="user">订单</el-menu-item>
          <el-menu-item index="/address" v-if="user">地址</el-menu-item>
          <el-sub-menu index="admin" v-if="user && user.role === 1">
            <template #title>管理</template>
            <el-menu-item index="/admin/products">商品管理</el-menu-item>
            <el-menu-item index="/admin/categories">分类管理</el-menu-item>
            <el-menu-item index="/admin/orders">订单管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
        <div class="user-info">
          <template v-if="user">
            <span>{{ user.username }}</span>
            <el-button link @click="logout">退出</el-button>
          </template>
          <template v-else>
            <el-button link @click="$router.push('/login')">登录</el-button>
            <el-button link @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    <el-main><router-view :key="$route.fullPath" @login-success="loadUser" /></el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted, provide } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()
const user = ref(null)

const loadUser = async () => {
  try {
    const res = await api.getUserInfo()
    user.value = res.data
  } catch {
    user.value = null
  }
}

onMounted(loadUser)

provide('user', user)
provide('loadUser', loadUser)

const logout = async () => {
  await api.logout()
  user.value = null
  router.push('/login')
}
</script>

<style>
body { margin: 0; }
.el-header { border-bottom: 1px solid #eee; }
.header-content { display: flex; align-items: center; height: 60px; }
.logo { font-size: 20px; font-weight: bold; cursor: pointer; margin-right: 20px; }
.el-menu { flex: 1; border: none; }
.user-info { margin-left: auto; }
</style>
