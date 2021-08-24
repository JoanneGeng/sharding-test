package com.example.shardingtest

import com.example.shardingtest.unit.DataSourceUtil
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory
import org.apache.shardingsphere.encrypt.api.config.EncryptRuleConfiguration
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptColumnRuleConfiguration
import org.apache.shardingsphere.encrypt.api.config.rule.EncryptTableRuleConfiguration
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.sql.DataSource


@Configuration
class EncryptDataSourceConfig {

    @Bean("encryptDateSource")
    fun getDataSource(): DataSource {
        return ShardingSphereDataSourceFactory.createDataSource(
            createDataSourceMap(),
            listOf(
                encryptRuleConfig()
            ),
            Properties()
        )
    }

    private fun createDataSourceMap(): Map<String, DataSource> {
        val result = mutableMapOf<String, DataSource>()
        result["encrypt"] = DataSourceUtil.createDataSource("encrypt")
        return result
    }

    private fun encryptRuleConfig(): EncryptRuleConfiguration {

        val props = Properties()
        props.setProperty("aes-key-value", "123456")

        val columnConfigAes =
            EncryptColumnRuleConfiguration("user_name", "user_name", "", "user_name_plain", "name_encryptor")

        val columnConfigTest = EncryptColumnRuleConfiguration("pwd", "pwd", "assisted_query_pwd", "", "pwd_encryptor")

        val encryptTableRuleConfig =
            EncryptTableRuleConfiguration("t_user", listOf(columnConfigAes, columnConfigTest))

        val encryptAlgorithmConfigs = mutableMapOf<String, ShardingSphereAlgorithmConfiguration>()
        encryptAlgorithmConfigs["name_encryptor"] = ShardingSphereAlgorithmConfiguration("AES", props)
        encryptAlgorithmConfigs["pwd_encryptor"] = ShardingSphereAlgorithmConfiguration("assistedTest", props)
        return EncryptRuleConfiguration(setOf(encryptTableRuleConfig), encryptAlgorithmConfigs)
    }
}