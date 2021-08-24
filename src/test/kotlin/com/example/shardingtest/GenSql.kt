package com.example.shardingtest

fun main() {

    val tables = listOf(
        "demo_write_ds_0",
        "demo_write_ds_0_read_0",
        "demo_write_ds_0_read_1",
        "demo_write_ds_1",
        "demo_write_ds_1_read_0",
        "demo_write_ds_1_read_1"
    )

    val result = tables.map {

        val sql = "drop table if exists %s.`t_order_%d`;\n" +
                "CREATE TABLE IF NOT EXISTS %s.`t_order_%d` (LIKE demo_ds_0.`t_order`);"

        val sql1 = sql.format(it, 0, it, 0)

        val sql2 = sql.format(it, 1, it, 1)
        "$sql1;\n" +
                "$sql2"
    }.joinToString("\n")

    println(result)


}