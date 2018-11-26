@echo off
xcopy L:\Doc\SVN\Work\OtherAS\test\testSerialPort\app\src\main L:\Doc\Git\AppInvTestSerialPort\app\src\main\ /S
xcopy L:\Doc\SVN\Work\OtherAS\test\testSerialPort\app\libs L:\Doc\Git\AppInvTestSerialPort\app\libs\ /S
copy L:\Doc\SVN\Work\OtherAS\test\testSerialPort\app\build.gradle L:\Doc\Git\AppInvTestSerialPort\app
pause
