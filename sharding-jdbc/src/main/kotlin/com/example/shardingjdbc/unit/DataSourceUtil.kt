package com.example.shardingjdbc.unit

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DataSourceUtil {

    private val userName = "root"
    private val passWord = "ps123456"
    private val driverClassName = "com.mysql.cj.jdbc.Driver"
    private val jdbcUrl =
        "jdbc:mysql://localhost:3306/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8"

    fun createDataSource(schema: String): DataSource {
        val ds1 = HikariDataSource()
        ds1.driverClassName = driverClassName
        ds1.jdbcUrl = jdbcUrl.format(schema)
        ds1.username = userName
        ds1.password = passWord
        return ds1
    }



}