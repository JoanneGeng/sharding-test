package com.example.shardingjdbc.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
class ReadWriteTest {

    @Resource(name = "readWriteDateSourceJdbcTemplate")
    private lateinit var jdbcTemplate: JdbcTemplate


    @PostMapping("/_read")
    fun read() {

        // 坑
        val sql1 =
            "select * from t_order where order_id = %d and user_id = %d"

        val param = listOf(Pair(1, 10), Pair(2, 1000))

        val result = param.map {
            val sql1 = sql1.format(it.first, it.second)
            jdbcTemplate.queryForList(sql1)
        }

    }

    @PostMapping("/_write")
    fun write() {
        // 坑 Insert statement does not support sharding table routing to multiple data nodes.
        val sql1 =
            "INSERT INTO t_order (user_id, order_id, title_id, column_id) VALUES (%d, %d, 't_order', '')"

//        Data truncation: Out of range value for column 'id' at row 1
        val param = listOf(Pair(1, 10), Pair(2, 1000))

        param.map {
            val sql1 = sql1.format(it.first, it.second)
            jdbcTemplate.execute(sql1)
        }
    }

}