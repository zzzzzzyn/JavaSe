### Shell 流程控制

#### if else

经常与test配合使用

##### if

```shell
if condition
then 
	command1
	command2
	...
	commandn
fi
```

写成一行

```shell
if [ $(ps -ef | grep -c "ssh") -gt 1 ]; then echo "true"; fi
```

##### if else

```shell
if condition
then 
	command1
	command2
	...
	commandn
else
	command1
	...
	commandn
fi
```

##### if else-if else

```shell
if condition
then 
	command
elif condition2
then
	command
else
	command
fi
```

#### for 循环

```shell
for var in item1 item2 ... itemN
do	
	command1
	command2
	...
	commandN
done
```

写成一行

```shell
for var in item1 item2 ... itemN; do command1; command2… done;
```

#### while 语句

```shell
while condition
do
	command
done
```

```shell
#!/bin/bash
int=1
while(( $int<=5 ))
do	
	echo $int
	let "int++"
done
```

#### until 循环

和while相反，当判断条件为true时推出

```shell
until condition
do	
	command
done
```

#### case 条件选择

```shell
case 值 in
	模式1)  command1
		   command2
		   ;;
	模式2)  command1
		   command2
		   ;;
	*)     command1		# 相当于default
		   command2		
esac
```

#### 跳出循环

##### break

break为跳出所有循环，相当于java中的break

##### continue

continue为跳出本次循环，相当于java中的continue

#### 

