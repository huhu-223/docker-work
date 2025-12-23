<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>用户注册</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" :loading="loading">注册</el-button>
          <el-button @click="$router.push('/login')">去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import api from '@/api'

const router = useRouter()
const form = ref({ username: '', password: '', phone: '', email: '' })
const loading = ref(false)

const register = async () => {
  loading.value = true
  try {
    await api.register(form.value)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } finally { loading.value = false }
}
</script>

<style scoped>
.login-container { display: flex; justify-content: center; padding-top: 100px; }
.login-card { width: 400px; }
h2 { text-align: center; }
</style>
