#!/bin/bash

echo "=== CI/CD 管道测试脚本 ==="
echo ""

# 1. 检查目录
echo "1. 检查CI/CD配置目录..."
if [ -d ".github/workflows" ]; then
    echo "   ✅ .github/workflows 目录存在"
    echo "   目录内容:"
    ls -la .github/workflows/
else
    echo "   ❌ .github/workflows 目录不存在"
    exit 1
fi

# 2. 检查配置文件
echo ""
echo "2. 检查CI/CD配置文件..."
FILES=("basic-ci.yml" "ci-pipeline.yml")
for file in "${FILES[@]}"; do
    if [ -f ".github/workflows/$file" ]; then
        echo "   ✅ $file 存在"
        echo "   文件大小: $(wc -l < .github/workflows/$file) 行"
    else
        echo "   ❌ $file 不存在"
    fi
done

# 3. 验证YAML语法
echo ""
echo "3. 验证YAML语法..."
if command -v yamllint &> /dev/null; then
    yamllint .github/workflows/ci-pipeline.yml || echo "   警告: YAML可能有语法问题"
else
    echo "   跳过YAML检查 (yamllint未安装)"
fi

# 4. 检查项目结构
echo ""
echo "4. 检查项目结构..."
echo "   项目根目录:"
ls -la | grep -E "\.(yml|sh|md)$" || true

echo ""
echo "=== 测试完成 ==="
echo "下一步: 执行 git add . 和 git commit"
