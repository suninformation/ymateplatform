@echo off

call setenv.bat

echo ---------------------------------------------------------
echo Install tomcat%TOMCAT_VERSION%%SERVICE_NAME% service...
echo ---------------------------------------------------------

call %CATALINA_HOME%\bin\service.bat install tomcat%TOMCAT_VERSION%%SERVICE_NAME%

pause