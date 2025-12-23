<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>用户登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" :loading="loading">登录</el-button>
          <el-button @click="$router.push('/register')">去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '@/api'

const router = useRouter()
const form = ref({ username: '', password: '' })
const loading = ref(false)
const loadUser = inject('loadUser')

const login = async () => {
  loading.value = true
  try {
    await api.login(form.value)
    ElMessage.success('登录成功')
    await loadUser()
    router.push('/products')
  } finally { loading.value = false }
}
</script>

<style scoped>
.login-container { display: flex; justify-content: center; padding-top: 100px; }
.login-card { width: 400px; }
h2 { text-align: center; }
</style>
