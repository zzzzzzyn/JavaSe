### echo命令

格式：

```shell
echo string
```

1. 普通字符串

```shell
echo "It is test"	
echo It is test		# 双引号可省略
```

2. 转义字符

```shell
echo "\"It is test\""
echo \"It is test\"  	# 双引号可省略
```

3. 显示变量

```shell
# read 命令从标准输入中读取一行,并把输入行的每个字段的值指定给 shell 变量
#!/bin/bash
read name
echo "$name It is a test"

执行：./test.sh
xyn					# 标准输入
xyn It is a test	# 标准输出
```

4. 显示换行

```shell
echo -e "ok! \n" 	# -e 开启转义
echo "It is a test"
```

5. 不显示换行

```shell
echo -e "ok! \c" 	# -e 开启转义 \c 不换行
echo "It is a test"
```

6. 显示结果定向至文件

```shell
echo "It is a test" > myfile
```

7. 原样输出字符串

```shell
echo '$name\"'			# 用单引号
```

8. 显示命令执行结果

```shell
echo `date`
```

