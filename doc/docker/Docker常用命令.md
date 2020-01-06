镜像命令

```java
docker search 镜像id		   			// 搜索镜像
docker pull 镜像id		   			// 获取镜像
docker images						 // 查看所有的镜像
docker rmi 镜像id			   			// 删除镜像
```

容器命令

```java
docker ps 							 // 列出运行中的容器
docker ps -a 						 // 列出所有容器
docker run 镜像id		          	    // 通过镜像构建容器
    									docker run -d -p 8080:8081 
                                        -v /usr/local/tomcat/:/usr/local/tomcat/conf/
                                        -v /usr/locat/tomcat/logs:/usr/local/tomcat/logs
                                        --name=tomcat8080 tocat
                                     	-d:守护线程，-p:端口映射，-v:挂载(本地路径:容器路径)
                                        上面解释:守护线程，端口映射，挂载conf，挂载logs
docker start 容器id					// 启动停止的容器
docker restart 容器id					// 重启容器
docker stop 容器id					// 停止容器
docker kill 容器id					// 强制停止容器(可同时停止多个)
docker exec -it 容器id  /bin/bash     // 进入容器
docker cp 容器id:容器文件 本地路径	   // 拷贝容器内文件到本地
    									docker cp e7q892:/usr/conf  /usr/local/
docker rm 容器id						// 删除容器
docker export 容器id       			// 导出容器：docker export nginx8080 > nginx.tar
docker import 容器id					// 导入容器为镜像：docker import ngxin.tar nginx8080
docker logs -f 容器id					// 输出容器内部的标准输出，类似tail -f，一般用于查看tomcat
```

> [菜鸟教程](https://www.runoob.com/docker/docker-dockerfile.html)

> [周立docker](http://www.itmuch.com/docker/00-docker-lession-index/)