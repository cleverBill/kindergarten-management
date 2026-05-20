@echo off
echo 正在连接MySQL数据库...

"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 student_management -e "
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS homework;
DROP TABLE IF EXISTS kindergarten_config;
DROP TABLE IF EXISTS reward;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;
"

echo.
echo 所有表已删除！
echo.
echo 请重启后端服务，Hibernate会自动创建所有表。
echo.
pause
