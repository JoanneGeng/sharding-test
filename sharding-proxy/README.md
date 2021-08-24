# ShardingSphere-proxy

```
git clone https://github.com/apache/shardingsphere
mvn clean install
cd shardingsphere-distribution/shardingsphere-proxy-distribution
mvn clean package -Prelease,docker

# docker images 查看镜像

# 跑起来
 docker run \
 -d -v /${your_work_dir}/conf:/opt/shardingsphere-proxy/conf \ 
 --link local-mysql:local-mysql \
 -e PORT=3308 \
 -p 13308:3308 \
 -d apache/shardingsphere-proxy:5.0.0-RC1-SNAPSHOT

```

## 非期望
```
The classpath is /opt/shardingsphere-proxy/conf:.:/opt/shardingsphere-proxy/lib/*:/opt/shardingsphere-proxy/ext-lib/*
Please check the STDOUT file: /opt/shardingsphere-proxy/logs/stdout.log
tail: unrecognized file system type 0x794c7630 for ‘/opt/shardingsphere-proxy/logs/stdout.log’. please report this to bug-coreutils@gnu.org. reverting to polling
Exception in thread "main" java.lang.RuntimeException: Failed to get driver instance for jdbcUrl=jdbc:mysql://127.0.0.1:3306/encrypt?serverTimezone=UTC&useSSL=false
```

添加JDBC驱动
lib 下放 mysql-connector-java-8.0.11.jar

