### 变量和字符串

#### 变量类型

运行shell时，会同时存在三种变量：

- **1) 局部变量** 局部变量在脚本或命令中定义，仅在当前shell实例中有效，其他shell启动的程序不能访问局部变量。
- **2) 环境变量** 所有的程序，包括shell启动的程序，都能访问环境变量，有些程序需要环境变量来保证其正常运行。必要的时候shell脚本也可以定义环境变量。
- **3) shell变量** shell变量是由shell程序设置的特殊变量。shell变量中有一部分是环境变量，有一部分是局部变量，这些变量保证了shell的正常运行

1. 普通变量：变量赋值时 = 两边不能有空格

```shell
#!/bin/bash				# 告知系统用什么解释器执行
my_name="xyn"			# 赋值
echo $my_name			# 输出，使用变量必须用$
my_name="xyn122"
echo ${my_name}			# {}可加可不加
```

2. 只读变量：readonly 变量名

```shell
#!/bin/bash
url="www.xyn.com"
readonly url
url="www.zyn.com"
```

3. 变量删除：unset 变量名，不能删除只读变量

```shell
#!/bin/bash
url="www.xyn.com"
readonly url
unset my_name
echo $my_name			# 变量删除后不输出任何东西
```

#### 字符串

1. 字符串：注意 单引号双引号 的替换问题

```shell
#!/bin/bash
str=hello
echo 'are you ok? $str! thank you! are very too match!'		# 单引号内变量不会替换
echo "are you ok? $str! thank you! are very too match!"      # 双引号内变量进行替换
```

2. 字符串拼接

```shell
#!/bin/bash
str1=hello
str2=hi
echo '单引号'$str1'单引号'              
echo "双引号"$str2"双引号"
echo $str1$str2
```

3. 字符串操作

```shell
#!/bin/bash
str='are you ok? hello! thank you! are very too match!'
echo $str								# 删除字符串
echo ${#str}							# 字符串长度
echo ${str:1:3}							# 截取字符串从第2个字符开始，截取三个字符
echo `expr index "$str" ao`				# 反引号`，查找子字符串，查询a或o首次出现的位置
```

