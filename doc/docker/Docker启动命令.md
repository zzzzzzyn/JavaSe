### MinIO

```shell
docker run -d -p9000:9000 --name minio9000 \
-v /root/minio/data/:/data \
-v /root/minio/config:/root/.minio \
-e "MINIO_ACCESS_KEY=minioadmin" \
-e "MINIO_SECRET_KEY=951301830" \
minio/minio server /data
```

### RabbitMq:management

由于是单机环境，所以就只开了5672和15672两个端口，并设置了初始用户名和密码

```shell
docker run -d -p5672:5672 -p15672:15672 \
-e RABBITMQ_DEFAULT_USER=rabbitadmin \
-e RABBITMQ_DEFAULT_PASS=951301830 \
--name rabbitmq15672 \
rabbitmq:management
```

### Redis

这里我将data文件夹(存放持久化文件)和redis-conf映射到了redis中

```shell
docker run redis -d -p6379:6379  \
-v /root/redis/data:/data \
-v /root/redis/redis-conf:/etc/redis/redis.conf  \
--name redis6379  \
redis-server /etc/redis/redis.conf
```

### Mongo

```shell

```

