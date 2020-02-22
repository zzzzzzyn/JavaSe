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

```shell
docker run -d -p5672:5672 -p15672:15672 --name rabbitmq15672 \
-e RABBITMQ_DEFAULT_USER=rabbitadmin \
-e RABBITMQ_DEFAULT_PASS=951301830 \
rabbitmq:management
```

### ELK

```shell

```

### Mongo

```shell

```

