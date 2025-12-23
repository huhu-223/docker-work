# 电商系统 Docker 部署文档

## 项目架构

```
Frontend(Nginx:80) → Backend(SpringBoot:8080) → MySQL(3306)
```

## 目录结构

```
docker-work/
├── backend/           # 后端源码 + Dockerfile
├── frontend/          # 前端源码 + Dockerfile + nginx.conf
├── database/          # 数据库初始化脚本
├── docs/              # 文档
└── docker-compose.yml # 编排文件
```

## 快速部署

```bash
# 克隆项目
git clone https://github.com/your-repo/docker-work.git
cd docker-work

# 启动服务
docker-compose up -d

# 查看状态
docker-compose ps
```

## 访问地址

- 前端: http://虚拟机IP:80
- 后端API: http://虚拟机IP:8080/api

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 普通用户 | user1 | 123456 |

## 端口映射

| 服务 | 端口 |
|------|------|
| 前端 | 80 |
| 后端 | 8080 |
| MySQL | 3306 |

## 常用命令

```bash
docker-compose logs -f    # 查看日志
docker-compose down       # 停止服务
docker-compose restart    # 重启
```

## 技术栈

- 后端: Spring Boot 3.5 + MyBatis + MySQL 8.0
- 前端: Vue 3 + Element Plus + Vite
- 容器: Docker + Docker Compose
- Web服务器: Nginx (Alpine)
