package com.example.shardingjdbc.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class JDBCTempleConfig {

    /**
     * 分表分库
     */
    @Bean("shardingTableJdbcTemplate")
    fun shardingTableJdbcTemplate(@Qualifier("shardingTable") shardingTable: DataSource): JdbcTemplate {
        return JdbcTemplate(shardingTable)
    }


    /**
     * 读写分离
     */
    @Bean("readWriteDateSourceJdbcTemplate")
    fun readWriteDateSourceJdbcTemplate(@Qualifier("readWriteDateSource") shardingTable: DataSource): JdbcTemplate {
        return JdbcTemplate(shardingTable)
    }

    /**
     * 加密
     */
    @Bean("encryptDateSourceJdbcTemplate")
    fun encryptDateSourceJdbcTemplate(@Qualifier("encryptDateSource") shardingTable: DataSource): JdbcTemplate {
        return JdbcTemplate(shardingTable)
    }
}