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

* Public Key Retrieval is not allowed
  allowPublicKeyRetrieval=true

## 分库分表
```yaml
rules:
- !SHARDING
  tables:
    t_order:
      actualDataNodes: demo_ds_${0..1}.t_order_${0..1}
      tableStrategy:
        standard:
          shardingColumn: order_id
          shardingAlgorithmName: t_order_inline
      keyGenerateStrategy:
        column: order_id
        keyGeneratorName: snowflake # 雪花算法

```


## 读写分离
```yml
rules:
- !READWRITE_SPLITTING
  dataSources:
    pr_ds:
      writeDataSourceName: demo_write_ds_0
      readDataSourceNames:
        - demo_write_ds_0_read_0
        - demo_write_ds_0_read_1
```

## 加密

```yaml
rules:
- !ENCRYPT
  encryptors:
    aes_encryptor:
      type: AES
      props:
        aes-key-value: 123456abc
    md5_encryptor:
      type: MD5
  tables:
    t_encrypt:
      columns:
        user_name:
          plainColumn: user_name_plain
          cipherColumn: user_cipher
          encryptorName: aes_encryptor
```

