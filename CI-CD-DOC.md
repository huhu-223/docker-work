# CI/CD 配置文档

## 配置文件
`.github/workflows/ci-cd.yml`

## 流水线设计

### 1. ✅ 项目验证
- 验证项目结构完整性
- 检查Docker和配置文件

### 2. 🏗️ 项目构建
- 构建Spring Boot后端
- 构建Vue.js前端
- 构建Docker镜像

### 3. 🧪 自动化测试
- 运行Java单元测试
- 生成测试报告
- 上传测试结果

### 4. 🔗 集成验证
- 验证容器化架构
- 检查服务依赖关系
- 验证网络配置

### 5. 🚀 部署准备
- 生成部署清单
- 准备生产环境部署

## 触发条件
- 推送到 main/develop 分支
- Pull Request到main分支
- 手动触发

## 技术特性
- 完整的5阶段流水线
- 超时控制防止卡死
- 测试报告自动生成
- 集成验证报告
- 部署准备清单

## 查看结果
访问: https://github.com/huhu-223/docker-work/actions
