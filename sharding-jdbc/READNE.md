# Sharding JDBC DEMO

> 使用 5.0.0-beta 实现简单分库分表，读写分离，加密功能</p>
> 本地 install tag 5.0.0-beta </p>
> 参考 https://github.com/apache/shardingsphere-example/ </p>
> springboot2.5.4 + JdbcTemplate + kotlin 1.5.21 + jdk 11 + sharding 5.0.0-RC1-SNAPSHOT

## 分表分库

* Step1：配置多数据源
```kotlin
private fun createDataSourceMap(): Map<String, DataSource> {
    val result = mutableMapOf<String, DataSource>()
    result["demo_ds_0"] = DataSourceUtil.createDataSource("demo_ds_0")
//        dataSourceMap["ds1"] = ds2 // key 要是库名 actualDataNodes
//        org.apache.shardingsphere.infra.metadata.schema.builder.TableMetaDataLoaderEngine.loadByDialect 抛 NPE
    result["demo_ds_1"] = DataSourceUtil.createDataSource("demo_ds_1")
    return result
}
```
这儿有个坑: Map<String, DataSource> 的key 要等于库名，否则会抛 NPE

* Step2： 配置分表规则/分库规则
```kotlin
 // 配置 t_order 表规则
val orderTableRuleConfig = ShardingTableRuleConfiguration("t_order", "demo_ds_\${0..1}.t_order_\${0..1}")


// 配置分库策略
orderTableRuleConfig.databaseShardingStrategy =
    StandardShardingStrategyConfiguration("user_id", "dbShardingAlgorithm")

// 配置分表策略
orderTableRuleConfig.tableShardingStrategy =
    StandardShardingStrategyConfiguration("order_id", "tableShardingAlgorithm")

// 配置分片规则
val shardingRuleConfig = ShardingRuleConfiguration()
shardingRuleConfig.tables.add(orderTableRuleConfig)

// 配置分库算法
val dbShardingAlgorithmProps = Properties()
dbShardingAlgorithmProps.setProperty("algorithm-expression", "demo_ds_\${user_id % 2}")
shardingRuleConfig.shardingAlgorithms["dbShardingAlgorithm"] =
    ShardingSphereAlgorithmConfiguration("INLINE", dbShardingAlgorithmProps)

// 配置分表算法
val tableShardingAlgorithmProps = Properties()
tableShardingAlgorithmProps.setProperty("algorithm-expression", "t_order_\${order_id % 2}")
shardingRuleConfig.shardingAlgorithms["tableShardingAlgorithm"] =
    ShardingSphereAlgorithmConfiguration("INLINE", tableShardingAlgorithmProps)

```

* Step3：数据源配置（ShardingSphereDataSource 数据源配置几乎都是一样的）
```kotlin
@Bean("shardingTable")
fun getDataSource(): DataSource {
    // 创建 ShardingSphereDataSource
    return ShardingSphereDataSourceFactory.createDataSource(
        createDataSourceMap(),
        listOf(shardingRuleConfig()),
        Properties()
    )
}
```

* Step4：配置不同 JdbcTemplate
，用注解直接使用
```kotlin
// 配置指定 JDBC
@Bean("shardingTableJdbcTemplate")
fun shardingTableJdbcTemplate(@Qualifier("shardingTable") shardingTable: DataSource): JdbcTemplate {
    return JdbcTemplate(shardingTable)
}
```

* Step5：测试

```kotlin

@Resource(name = "shardingTableJdbcTemplate")
private lateinit var jdbcTemplate: JdbcTemplate

@PostMapping("/_sharding_table")
fun test() {

    // 坑
    val sql1 =
        "INSERT INTO t_order (user_id, order_id, title_id, column_id) VALUES (%d, %d, 't_order', '')"

    //  Data truncation: Out of range value for column 'id' at row 1
    // 开始数据库的id 类型是int型
    val param = listOf(Pair(1, 10), Pair(2, 1000))

    param.map {
        val sql1 = sql1.format(it.first, it.second)
        jdbcTemplate.execute(sql1)
    }
}
```
这儿还有一个问题，在写测试 sql 的时候，不小心加上了库名，一直报错「数据库不存在」


## 读写分离
多数据源配置，JdbcTemplate，测试和上面一样，增加读写规则配置即可

```kotlin
// 配置读写规则
private fun createReadwriteSplittingConfiguration(): ReadwriteSplittingRuleConfiguration {
        val dataSourceConfiguration1 = ReadwriteSplittingDataSourceRuleConfiguration(
            "ds_0", "", "demo_write_ds_0", listOf("demo_write_ds_0_read_0", "demo_write_ds_0_read_1"), null
        )
        val dataSourceConfiguration2 = ReadwriteSplittingDataSourceRuleConfiguration(
            "ds_1", "", "demo_write_ds_1", listOf("demo_write_ds_1_read_0", "demo_write_ds_1_read_1"), null
        )
        return ReadwriteSplittingRuleConfiguration(
            listOf(dataSourceConfiguration1, dataSourceConfiguration2),
            emptyMap()
        )
}
```

## 数据加密

数据加密部分不一样 
```kotlin
private fun encryptRuleConfig(): EncryptRuleConfiguration {

        val props = Properties()
        props.setProperty("aes-key-value", "123456")

        val columnConfigAes =
            EncryptColumnRuleConfiguration("user_name", "user_name", "", "user_name_plain", "name_encryptor")

        val columnConfigTest = EncryptColumnRuleConfiguration("pwd", "pwd", "assisted_query_pwd", "", "pwd_encryptor")

        val encryptTableRuleConfig =
            EncryptTableRuleConfiguration("t_user", listOf(columnConfigAes, columnConfigTest))

        val encryptAlgorithmConfigs = mutableMapOf<String, ShardingSphereAlgorithmConfiguration>()
        // 加密算法 
        encryptAlgorithmConfigs["name_encryptor"] = ShardingSphereAlgorithmConfiguration("AES", props)
        encryptAlgorithmConfigs["pwd_encryptor"] = ShardingSphereAlgorithmConfiguration("assistedTest", props)
        return EncryptRuleConfiguration(setOf(encryptTableRuleConfig), encryptAlgorithmConfigs)
}
```
这儿有个坑，配置好encryptRuleConfig之后，服务启动报错
```kotlin
 solve No implementation class load from SPI `org.apache.shardingsphere.encrypt.spi.EncryptAlgorithm` with type `assistedTest`.
```
这儿需要实现 QueryAssistedEncryptAlgorithm 接口，并配置 ```/META-INF/services```
```kotlin
class TestAssistedEncryptAlgorithm: QueryAssistedEncryptAlgorithm {

    override fun init() {}

    override fun encrypt(plaintext: Any?): String {
        return "encryptValue"
    }

    override fun decrypt(ciphertext: String?): Any {
        return "decryptValue"
    }

    override fun queryAssistedEncrypt(plaintext: String?): String {
        return "assistedEncryptValue"
    }

    override fun getType(): String {
        return "assistedTest"
    }
}
```

