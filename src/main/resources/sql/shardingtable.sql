create schema demo_ds_0;
create schema demo_ds_1;

drop table demo_ds_0.`t_order`;
CREATE TABLE IF NOT EXISTS demo_ds_0.`t_order`
(
    `id`       bigint UNSIGNED AUTO_INCREMENT,
    `user_id`  bigint NOT NULL,
    `order_id` bigint NOT NULL,
    title_id   varchar(32),
    column_id  varchar(32),
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

drop table demo_ds_0.`t_order_item`;
CREATE TABLE IF NOT EXISTS demo_ds_0.`t_order_item`
(
    `id`       bigint UNSIGNED AUTO_INCREMENT,
    `user_id`  bigint NOT NULL,
    `order_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

drop table demo_ds_0.`t_order_0`;
CREATE TABLE IF NOT EXISTS demo_ds_0.`t_order_0` (LIKE demo_ds_0.`t_order`);
drop table demo_ds_0.`t_order_item_0`;
CREATE TABLE IF NOT EXISTS demo_ds_0.`t_order_item_0` (LIKE demo_ds_0.`t_order_item`);
drop table demo_ds_1.`t_order`;
CREATE TABLE IF NOT EXISTS demo_ds_1.`t_order` (LIKE demo_ds_0.`t_order`);
drop table demo_ds_1.`t_order_item`;
CREATE TABLE IF NOT EXISTS demo_ds_1.`t_order_item` (LIKE demo_ds_0.`t_order_item`);
drop table demo_ds_1.`t_order_0`;
CREATE TABLE IF NOT EXISTS demo_ds_1.`t_order_0` (LIKE demo_ds_0.`t_order`);
drop table demo_ds_1.`t_order_item_0`;
CREATE TABLE IF NOT EXISTS demo_ds_1.`t_order_item_0` (LIKE demo_ds_0.`t_order_item`);