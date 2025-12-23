# Dockerfile 编写说明

## 后端 Dockerfile

### 多阶段构建

```dockerfile
# 第一阶段：构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder
```

使用多阶段构建的优势：
1. **减小镜像体积**：最终镜像只包含运行时需要的文件
2. **提高安全性**：不包含构建工具和源代码
3. **加快部署速度**：镜像更小，传输更快

### 依赖缓存优化

```dockerfile
COPY pom.xml .
RUN mvn dependency:go-offline -B
```

先复制pom.xml并下载依赖，利用Docker层缓存：
- 只有pom.xml变化时才重新下载依赖
- 源代码变化不会触发依赖重新下载

### 非root用户

```dockerfile
RUN addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -D appuser
USER appuser
```

安全最佳实践：
- 创建专用用户运行应用
- 避免使用root用户

### 健康检查

```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/category/list || exit 1
```

参数说明：
- `interval`: 检查间隔
- `timeout`: 超时时间
- `start-period`: 启动等待时间
- `retries`: 重试次数

## 前端 Dockerfile

### Nginx配置

```dockerfile
FROM nginx:alpine
COPY nginx.conf /etc/nginx/nginx.conf
```

使用Alpine版本减小镜像体积。

### API代理配置

```nginx
location /api/ {
    proxy_pass http://backend:8080/api/;
}
```

通过Nginx反向代理将API请求转发到后端服务。

## 镜像大小对比

| 构建方式 | 镜像大小 |
|----------|----------|
| 单阶段构建 | ~500MB |
| 多阶段构建 | ~200MB |

## 最佳实践总结

1. **使用多阶段构建**减小镜像体积
2. **利用层缓存**加速构建
3. **使用非root用户**提高安全性
4. **配置健康检查**确保服务可用
5. **使用Alpine基础镜像**减小体积
6. **合理设置环境变量**便于配置管理
