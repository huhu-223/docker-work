import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({ baseURL: '/api', timeout: 10000 })

api.interceptors.response.use(
  res => res.data,
  err => { 
    ElMessage.error(err.response?.data?.message || '请求失败')
    return Promise.reject(err) 
  }
)

export default {
  // 用户
  login: data => api.post('/user/login', data),
  register: data => api.post('/user/register', data),
  logout: () => api.post('/user/logout'),
  getUserInfo: () => api.get('/user/info'),
  // 商品
  getProducts: params => api.get('/product/list', { params }),
  getAllProducts: params => api.get('/product/listAll', { params }),
  getProduct: id => api.get(`/product/${id}`),
  createProduct: data => api.post('/product', data),
  updateProduct: (id, data) => api.put(`/product/${id}`, data),
  deleteProduct: id => api.delete(`/product/${id}`),
  // 分类
  getCategories: () => api.get('/category/list'),
  createCategory: data => api.post('/category', data),
  updateCategory: (id, data) => api.put(`/category/${id}`, data),
  deleteCategory: id => api.delete(`/category/${id}`),
  // 购物车
  getCart: () => api.get('/cart'),
  addToCart: data => api.post('/cart', data),
  updateCartItem: (id, data) => api.put(`/cart/${id}`, data),
  removeCartItem: id => api.delete(`/cart/${id}`),
  clearCart: () => api.delete('/cart/clear'),
  // 地址
  getAddresses: () => api.get('/address/list'),
  createAddress: data => api.post('/address', data),
  updateAddress: (id, data) => api.put(`/address/${id}`, data),
  deleteAddress: id => api.delete(`/address/${id}`),
  setDefaultAddress: id => api.put(`/address/${id}/default`),
  // 订单
  getOrders: params => api.get('/order/list', { params }),
  getAllOrders: params => api.get('/order/listAll', { params }),
  getOrder: id => api.get(`/order/${id}`),
  createOrder: data => api.post('/order', data),
  cancelOrder: id => api.put(`/order/${id}/cancel`),
  updateOrderStatus: (id, data) => api.put(`/order/${id}/status`, data)
}
