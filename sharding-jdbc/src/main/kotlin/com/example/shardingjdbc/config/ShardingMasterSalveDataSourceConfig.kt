package com.example.shardingjdbc.config

import com.example.shardingjdbc.unit.DataSourceUtil
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.sql.DataSource


@Configuration
class ShardingMasterSalveDataSourceConfig {


    @Bean("readWriteDateSource")
    fun getDataSource(): DataSource {
        return ShardingSphereDataSourceFactory.createDataSource(
            createDataSourceMap(),
            listOf(
                createShardingRuleConfiguration(),
                createReadwriteSplittingConfiguration()
            ),
            Properties()
        )
    }

    private fun createDataSourceMap(): Map<String, DataSource> {
        val result = mutableMapOf<String, DataSource>()
        result["demo_write_ds_0"] = DataSourceUtil.createDataSource("demo_write_ds_0")
        result["demo_write_ds_0_read_0"] = DataSourceUtil.createDataSource("demo_write_ds_0_read_0")
        result["demo_write_ds_0_read_1"] = DataSourceUtil.createDataSource("demo_write_ds_0_read_1")
        result["demo_write_ds_1"] = DataSourceUtil.createDataSource("demo_write_ds_1")
        result["demo_write_ds_1_read_0"] = DataSourceUtil.createDataSource("demo_write_ds_1_read_0")
        result["demo_write_ds_1_read_1"] = DataSourceUtil.createDataSource("demo_write_ds_1_read_1")
        return result
    }

    private fun createShardingRuleConfiguration(): ShardingRuleConfiguration {
        val result = ShardingRuleConfiguration()
        result.tables.add(createOrderTableRuleConfiguration())
        result.tables.add(createOrderItemTableRuleConfiguration())
        result.bindingTableGroups.add("t_order, t_order_item")
        result.broadcastTables.add("t_address")
        result.defaultDatabaseShardingStrategy = StandardShardingStrategyConfiguration("user_id", "standard_test_db")
        result.defaultTableShardingStrategy = StandardShardingStrategyConfiguration("order_id", "standard_test_tbl")
//        result.shardingAlgorithms["standard_test_db"] =
//            ShardingSphereAlgorithmConfiguration("STANDARD_TEST_DB", Properties())
//        result.shardingAlgorithms["standard_test_tbl"] =
//            ShardingSphereAlgorithmConfiguration("STANDARD_TEST_TBL", Properties())
        result.keyGenerators["snowflake"] = ShardingSphereAlgorithmConfiguration("SNOWFLAKE", getProperties())
        return result
    }

    private fun createOrderTableRuleConfiguration(): ShardingTableRuleConfiguration {
        val result = ShardingTableRuleConfiguration("t_order", "ds_\${0..1}.t_order_\${[0, 1]}")
        result.keyGenerateStrategy = KeyGenerateStrategyConfiguration("order_id", "snowflake")
        return result
    }

    private fun createOrderItemTableRuleConfiguration(): ShardingTableRuleConfiguration? {
        val result = ShardingTableRuleConfiguration("t_order_item", "ds_\${0..1}.t_order_item_\${[0, 1]}")
        result.keyGenerateStrategy = KeyGenerateStrategyConfiguration("order_item_id", "snowflake")
        return result
    }

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

    private fun getProperties(): Properties {
        val result = Properties()
        result.setProperty("worker-id", "123")
        return result
    }
}

