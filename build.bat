@echo off
cls

set path=%path%;C:\Program Files\Java\jdk1.8.0_101\bin;C:\2120\Java\ant-1.9.6

::if "%2" == "test" goto test
if "%1" == "build" goto build

ant run
goto end

:build
ant build
goto end

end:
