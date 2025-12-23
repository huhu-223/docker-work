

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ecommerce_database DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce_database;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    role TINYINT DEFAULT 0 COMMENT '角色:0普通用户,1管理员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    image_url VARCHAR(500) COMMENT '图片地址',
    category_id BIGINT COMMENT '分类ID',
    status TINYINT DEFAULT 1 COMMENT '状态:0下架,1上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 收货地址表
CREATE TABLE IF NOT EXISTS address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    detail_address VARCHAR(200) NOT NULL COMMENT '详细地址',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认:0否,1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车项ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT DEFAULT 1 COMMENT '数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    status TINYINT DEFAULT 0 COMMENT '状态:0待付款,1已付款,2已发货,3已完成,4已取消',
    receiver_name VARCHAR(50) COMMENT '收货人',
    receiver_phone VARCHAR(20) COMMENT '收货电话',
    receiver_address VARCHAR(500) COMMENT '收货地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    pay_time DATETIME COMMENT '支付时间',
    ship_time DATETIME COMMENT '发货时间',
    complete_time DATETIME COMMENT '完成时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) COMMENT '商品名称',
    product_price DECIMAL(10,2) COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10,2) COMMENT '小计金额'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';



-- 用户数据
INSERT INTO user (username, password, phone, role) VALUES 
('admin', '123456', '13800000000', 1),
('user1', '123456', '13800000001', 0),
('user2', '123456', '13800000002', 0);

-- 分类数据
INSERT INTO category (name, sort_order) VALUES 
('电子产品', 1), 
('服装', 2), 
('食品', 3),
('家居用品', 4);

-- 商品数据
INSERT INTO product (name, price, stock, category_id, description) VALUES 
('iPhone 15', 6999.00, 100, 1, '苹果最新款手机'),
('MacBook Pro', 12999.00, 50, 1, '苹果笔记本电脑'),
('华为Mate60', 5999.00, 80, 1, '华为旗舰手机'),
('T恤', 99.00, 200, 2, '纯棉T恤'),
('牛仔裤', 199.00, 150, 2, '经典牛仔裤'),
('牛奶', 39.90, 500, 3, '纯牛奶1L装'),
('面包', 15.00, 300, 3, '全麦面包'),
('台灯', 89.00, 100, 4, 'LED护眼台灯');
