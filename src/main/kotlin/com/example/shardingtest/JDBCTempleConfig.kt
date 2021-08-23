package com.example.shardingtest

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class JDBCTempleConfig {

    @Bean("shardingTableJdbcTemplate")
    fun shardingTableJdbcTemplate(@Qualifier("shardingTable") shardingTable: DataSource): JdbcTemplate {
        return JdbcTemplate(shardingTable)
    }


    @Bean("readWriteDateSourceJdbcTemplate")
    fun readWriteDateSourceJdbcTemplate(@Qualifier("readWriteDateSource") shardingTable: DataSource): JdbcTemplate {
        return JdbcTemplate(shardingTable)
    }
}