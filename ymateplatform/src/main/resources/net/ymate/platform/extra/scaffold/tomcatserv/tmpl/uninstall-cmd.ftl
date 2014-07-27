@echo off

call setenv.bat

echo ---------------------------------------------------------
echo Uninstall tomcat%TOMCAT_VERSION%%SERVICE_NAME% service...
echo ---------------------------------------------------------

call %CATALINA_HOME%\bin\service.bat remove tomcat%TOMCAT_VERSION%%SERVICE_NAME%

pause