package com.example.shardingjdbc.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource


@RestController
class EncryptTest {

    @Resource(name = "encryptDateSourceJdbcTemplate")
    private lateinit var jdbcTemplate: JdbcTemplate


    @PostMapping("/_encrypt")
    fun encrypt() {
        val sql = "INSERT INTO t_user (user_name, pwd)\n" +
                "VALUES ('userName', 'pwd');"
        jdbcTemplate.execute(sql)

    }



}