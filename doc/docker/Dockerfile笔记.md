### Dockerfile

#### FROM

指明基础镜像，新镜像在基础镜像上做定制，格式为`FROM openjdk:8`获取`FROM centos:7`

#### MAINTAINER

指明镜像维护者及联系方式(谁弄的这个玩意儿)，格式(二选一)为`MAINTAINER xyn <951301830@qq.com>`或`LABEL maintainer="xuyanan"`

#### RUN

构建镜像时执行的shell命令或json，格式为`RUN ["yum", "install", "httpd"]`或`RUN yum install httpd`

#### CMD

启动容器时执行的shell命令或json，格式为`CMD ["java","-jar","app.jar"]`或`CMD java -jar app.jar`

#### EXPOSE

声明容器运行的服务端口，格式为`EXPOSE 8080`

#### ENV

设置环境内的环境变量，格式为`ENV JAVA_HOME /usr/local/jdk18`

#### ADD

拷贝文件或目录到镜像中，格式为`ADD <src>...<dest>`或`ADD nginx.tar.gz /usr/local`或`ADD http://nginx.com/nginx.tar.gz /usr/local`，**url或压缩包自动解压**

#### COPY

拷贝文件或目录到镜像，格式同ADD，不支持解压

#### ENTRYPOINT

启动容器执行的shell命令，同CMD类似只是由ENTRYPOINT启动的程序**不会被docker run命令行指定的参数所覆盖**，而且，**这些命令行参数会被当作参数传递给ENTRYPOINT指定指定的程序**，**Dockerfile文件中也可以存在多个ENTRYPOINT指令，但仅有最后一个会生效**

#### VOLUME

指定容器挂载点到宿主机自动生成的目录或其他容器，格式为`VOLUME ["/var/lib/mysql"]`，**一般不会在Dockerfile中用到，更常见的还是在docker run的时候指定-v数据卷**

#### USER

为RUN、CMD和ENTRYPOINT执行Shell命令指定运行用户，格式为`USER <user>[:<usergroup>]`或`USER <UID>[:<UID>]`或`USER xyn`

#### WORKDIR

为RUN、CMD、ENTRYPOINT以及COPY和AND设置工作目录，格式为`WORKDIR /data`

#### ARG

构建镜像时，执行一些参数，格式为

```dockerfile
FROM centos:7
ARG user # ARG user=root
USER $user
```

在docker build时可以带上自定义参数user

```dockerfile
docker build --build-arg user=aaa Dockerfile .
```

![逗比的理解图](.\img\dockerfile.png)

> [Edison zhou](https://www.cnblogs.com/edisonchou/p/dockerfile_inside_introduction.html)