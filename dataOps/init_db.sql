-- 安装postgis扩展
CREATE EXTENSION postgis;
-- 安装pg_trgm扩展
CREATE EXTENSION pg_trgm;
-- 数据 Schema：存放大量数据
CREATE SCHEMA data AUTHORIZATION postgres;
COMMENT ON SCHEMA data IS '数据存储';
-- UDF Schema：自定义函数
CREATE SCHEMA udf AUTHORIZATION postgres;
COMMENT ON SCHEMA udf IS '用户自定义函数';

