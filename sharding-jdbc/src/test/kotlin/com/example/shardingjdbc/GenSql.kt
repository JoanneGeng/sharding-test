package com.example.shardingjdbc

object GenSql {

    fun genReadWrite(): String {
        val dataBases = listOf(
            "demo_write_ds_0",
            "demo_write_ds_0_read_0",
            "demo_write_ds_0_read_1",
            "demo_write_ds_1",
            "demo_write_ds_1_read_0",
            "demo_write_ds_1_read_1"
        )

        val sql = "DROP TABLE IF EXISTS %s.`t_order_%d`;\n" +
                "CREATE TABLE %s.`t_order_%d` (LIKE demo_ds_0.`t_order`);"

        return dataBases.joinToString("\n") {
            val sql1 = sql.format(it, 0, it, 0)
            val sql2 = sql.format(it, 1, it, 1)

            sql1 + "\n" + sql2
        }
    }

    fun genEncrypt(): String {
        val dataBases = listOf(
            "t_user"
        )

        val sql = "DROP TABLE IF EXISTS encrypt.`%s`;\n" +
                "create table encrypt.`%s`\n" +
                "(\n" +
                "    id                 bigint unsigned auto_increment\n" +
                "        primary key,\n" +
                "    user_name          varchar(255) null,\n" +
                "    user_name_plain    varchar(255) null,\n" +
                "    pwd                varchar(255) null,\n" +
                "    assisted_query_pwd varchar(255) null\n" +
                ")\n" +
                "    charset = utf8;"

        return dataBases.joinToString("\n") {
            sql.format(it, it)

        }
    }


}

fun main() {
//    println(GenSql.genReadWrite())
    println(GenSql.genEncrypt())
}