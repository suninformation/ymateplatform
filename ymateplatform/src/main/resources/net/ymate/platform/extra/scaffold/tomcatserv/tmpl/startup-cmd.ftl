@echo off

call setenv.bat

echo ---------------------------------------------------------
echo Startup tomcat%TOMCAT_VERSION%%SERVICE_NAME% service...
echo ---------------------------------------------------------

call net start tomcat%TOMCAT_VERSION%%SERVICE_NAME%

pause