package com.example.shardingproxy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShardingProxyApplication

fun main(args: Array<String>) {
    runApplication<ShardingProxyApplication>(*args)
}
