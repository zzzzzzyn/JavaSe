### Shell文件包含

格式：

```shell
. filename   # 注意点号(.)和文件名中间有一空格
# 或
source filename
```

```shell
. ./test.sh			# 将test.sh文件包含进来
echo "测试文件"

source ./test.sh
echo "测试文件"
```

