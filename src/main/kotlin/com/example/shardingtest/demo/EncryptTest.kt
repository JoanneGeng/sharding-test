package com.example.shardingtest.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource


@RestController
class EncryptTest {

    @Resource(name = "encryptDateSourceJdbcTemplate")
    private lateinit var jdbcTemplate: JdbcTemplate


    fun encrypt() {

    }



}