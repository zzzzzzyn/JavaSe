```shell
#!/bin/bash
# 备份war包
tomcat_webapps="/usr/local/work/tomcat/webapps/"	# tomcat的webapps路径
sour="/usr/local/work/shell/"						# 备份到的文件夹下
war_file="xxx.war"									# 需要备份的war
dire_file=$(date "+%Y%m%d%H%M%S")					# 备份文件夹
cd $tomcat_webapps
if [ -e $war_file ]
then    									# 备份到source
    if [ -e $sour$dire_file ]               # 这其实不用判断，文件夹命名精确到了毫秒
    then
       	cp -f $tomcat_webapps$war_file $sour$dire_file
    else
        mkdir $sour$dire_file
        cp -f $tomcat_webapps$war_file $sour$dire_file
        echo "备份成功"
    fi
fi
```

