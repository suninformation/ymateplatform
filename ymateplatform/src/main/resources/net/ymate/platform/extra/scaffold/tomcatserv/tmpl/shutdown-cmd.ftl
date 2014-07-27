@echo off

call setenv.bat

echo ---------------------------------------------------------
echo Shutdown tomcat%TOMCAT_VERSION%%SERVICE_NAME% service...
echo ---------------------------------------------------------

call net stop tomcat%TOMCAT_VERSION%%SERVICE_NAME%

pause