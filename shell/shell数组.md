### 数组

1. 数组定义

```shell
数组名=(值1 值2 ... 值n)
```

2. 数组操作

```shell
#!/bin/bash
# 数组定义
array_name=(
xyn1
zyn2
)
# 数组赋值
array_name[3]=xyn2          # 数组赋值
array_name[4]=zyn2
# 输出数组某个元素
echo ${array_name[0]}		# 获取下标0的数组
echo ${array_name[1]}
# 获取所有数组元素
# -----start-----
echo ${array_name[@]}		# 输出所有数组元素
# 或者
echo ${array_name[*]}
# ------end------
# 获取数组元素个数
# -----start-----
length1=${#array_name[@]}	# 输出元素个数
# 或者
length2=${#array_name[*]}
echo $length1  $length2
# ------end------
```