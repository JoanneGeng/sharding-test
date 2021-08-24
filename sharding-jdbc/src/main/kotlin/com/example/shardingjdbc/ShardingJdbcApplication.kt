package com.example.shardingjdbc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShardingJdbcApplication

fun main(args: Array<String>) {

    try {
        runApplication<ShardingJdbcApplication>(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
