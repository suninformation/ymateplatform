@echo off

call setenv.bat

echo ---------------------------------------------------------
echo Manage/Configuration tomcat%TOMCAT_VERSION%%SERVICE_NAME% service...
echo ---------------------------------------------------------

call %CATALINA_HOME%\bin\tomcat%TOMCAT_VERSION%w.exe //MS//tomcattomcat%TOMCAT_VERSION%%SERVICE_NAME%

pause