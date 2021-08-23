package com.example.shardingtest.unit

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DataSourceUtil {

    private val username = "root"
    private val password = "ps123456"
    private val driverClassName = "com.mysql.cj.jdbc.Driver"
    private val jdbcUrl =
        "jdbc:mysql://localhost:3306/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8"

    fun createDataSource(schema: String): DataSource {
        val ds1 = HikariDataSource()
        ds1.driverClassName = driverClassName
        ds1.jdbcUrl = jdbcUrl.format(schema)
        ds1.username = username
        ds1.password = password
        return ds1
    }



}