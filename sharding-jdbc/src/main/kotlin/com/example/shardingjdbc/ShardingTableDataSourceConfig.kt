package com.example.shardingjdbc

import com.example.shardingjdbc.unit.DataSourceUtil
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.sql.DataSource

@Configuration
class ShardingTableDataSourceConfig {

    private fun createDataSourceMap(): Map<String, DataSource> {
        val result = mutableMapOf<String, DataSource>()
        result["demo_ds_0"] = DataSourceUtil.createDataSource("demo_ds_0")
//        dataSourceMap["ds1"] = ds2 // key 要是库名 actualDataNodes
//        org.apache.shardingsphere.infra.metadata.schema.builder.TableMetaDataLoaderEngine.loadByDialect 抛 NPE
        result["demo_ds_1"] = DataSourceUtil.createDataSource("demo_ds_1")
        return result
    }

    @Bean("shardingTable")
    fun shardingTable(): DataSource {

        // 配置 t_order 表规则
        val orderTableRuleConfig = ShardingTableRuleConfiguration("t_order", "demo_ds_\${0..1}.t_order_\${0..1}")


        // 配置分库策略
        orderTableRuleConfig.databaseShardingStrategy =
            StandardShardingStrategyConfiguration("user_id", "dbShardingAlgorithm")

        // 配置分表策略
        orderTableRuleConfig.tableShardingStrategy =
            StandardShardingStrategyConfiguration("order_id", "tableShardingAlgorithm")

        // 配置 t_order_item 表规则
        val orderItemTableRuleConfig = ShardingTableRuleConfiguration("t_order_item", "demo_ds_\${0..1}.t_order_item_\${0..1}")


        // 配置分库策略
        orderItemTableRuleConfig.databaseShardingStrategy =
            StandardShardingStrategyConfiguration("user_id", "dbShardingAlgorithm")


        // 配置分表策略
        orderItemTableRuleConfig.tableShardingStrategy =
            StandardShardingStrategyConfiguration("order_id", "tableShardingAlgorithm")


        // 配置分片规则
        val shardingRuleConfig = ShardingRuleConfiguration()
        shardingRuleConfig.tables.add(orderTableRuleConfig)
        shardingRuleConfig.tables.add(orderItemTableRuleConfig)


        val dbShardingAlgorithmProps = Properties()
        dbShardingAlgorithmProps.setProperty("algorithm-expression", "demo_ds_\${user_id % 2}")
        shardingRuleConfig.shardingAlgorithms["dbShardingAlgorithm"] =
            ShardingSphereAlgorithmConfiguration("INLINE", dbShardingAlgorithmProps)

        // 配置分表算法
        val tableShardingAlgorithmProps = Properties()
        tableShardingAlgorithmProps.setProperty("algorithm-expression", "t_order_\${order_id % 2}")
        shardingRuleConfig.shardingAlgorithms["tableShardingAlgorithm"] =
            ShardingSphereAlgorithmConfiguration("INLINE", tableShardingAlgorithmProps)


        // 创建 ShardingSphereDataSource
        return ShardingSphereDataSourceFactory.createDataSource(
            createDataSourceMap(),
            listOf(shardingRuleConfig),
            Properties()
        )


    }

}

fun main() {

    val dataSource = ShardingTableDataSourceConfig().shardingTable()

    val sql =
        "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?"

    dataSource.connection.use { conn ->
        conn.prepareStatement(sql).use { ps ->
            ps.setInt(1, 10)
            ps.setInt(2, 1000)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    println(rs)
                }
            }
        }
    }

}