package com.example.shardingjdbc.unit

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DataSourceUtil {

    private const val userName = "root"
    private const val passWord = "ps123456"
    private const val driverClassName = "com.mysql.cj.jdbc.Driver"
    private const val jdbcUrl =
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