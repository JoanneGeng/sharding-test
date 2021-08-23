package com.example.shardingtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShardingTestApplication

fun main(args: Array<String>) {
    runApplication<ShardingTestApplication>(*args)
}
