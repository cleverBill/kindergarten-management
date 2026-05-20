@echo off
echo 正在创建 kindergarten_config 表...

"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 student_management < "c:\Users\64333\Documents\trae_projects\AI study\backend\create_kindergarten_config.sql"

echo.
echo 表创建成功！
echo.
pause
