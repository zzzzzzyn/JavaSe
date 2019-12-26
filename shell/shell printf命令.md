### printf命令

格式：

- format-string：格式控制字符串
- arguments：参数列表

```shell
prinf format-string [arguments...]
```

```shell
$ echo "Hello, Shell"
$ printf "Hello, Shell\n"
```

```shell
#!/bin/bash
printf "%-10s %-8s %4s\n" 		姓名 性别 体重kg	
printf "%-10s %-8s %4.2f\n" 	xyn 男 60.1
printf "%-10s %-8s %4.2f\n" 	byz 男 60.2
printf "%-10s %-8s %4.2f\n" 	hsy 男 60.3
```

%s %c %d %f都是格式替代符(这里可以对照java的String.format对照记忆)

%-10s：长度为10的字符串，-(减号)表示左对齐，s表示字符串，不足10个以空格填充，超过10个则全部显示

%-4.2f：格式化为小数，.2保留2位小数

```shell
#!/bin/bash
# format-string为双引号
printf "%d %s\n" 1 "abc"
# 单引号与双引号效果一样 
printf '%d %s\n' 1 "abc" 
# 没有引号也可以输出
printf %s abcdef
# 格式只指定了一个参数，但多出的参数仍然会按照该格式输出，format-string 被重用
printf %s abc def
printf "%s\n" abc def
printf "%s %s %s\n" a b c d e f g h i j
# 如果没有 arguments，那么 %s 用NULL代替，%d 用 0 代替
printf "%s and %d \n" 
```

