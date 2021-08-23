package com.example.shardingtest.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
class ReadWriteTest {

    @Resource(name = "readWriteDateSourceJdbcTemplate")
    private lateinit var jdbcTemplate: JdbcTemplate

}