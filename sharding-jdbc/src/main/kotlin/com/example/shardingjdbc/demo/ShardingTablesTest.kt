package com.example.shardingjdbc.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource


@RestController
class ShardingTablesTest {

    @Resource(name = "shardingTableJdbcTemplate")
    private lateinit var jdbcTemplate: JdbcTemplate

    @PostMapping("/_sharding_create")
    fun createTable() {

        // 这样玩不行的
        val torder = "CREATE TABLE IF NOT EXISTS demo_ds_%d.`t_order_%d` (LIKE demo_ds_0.`t_order`)"
        val tOrderItem = "CREATE TABLE IF NOT EXISTS demo_ds_%d.`t_order_item_%d` (LIKE demo_ds_0.`t_order_item`)"

        for (i in 0 until 2) {
            jdbcTemplate.execute(torder.format(i,i))
            jdbcTemplate.execute(tOrderItem.format(i,i))
        }

    }


    @PostMapping("/_sharding_table")
    fun test() {

        // 坑
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