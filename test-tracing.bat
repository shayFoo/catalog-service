@echo off
echo Starting application...
start /B gradlew.bat bootRun > app.log 2>&1

echo Waiting for application to start...
timeout /t 15 /nobreak > nul

echo Testing endpoint...
curl -s http://localhost:9001/books

echo.
echo Checking logs for traceId...
findstr /C:"traceId" app.log

echo.
echo Done. Check app.log for full details.

