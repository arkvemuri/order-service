@echo off
REM Security Check Script for Order Service (Windows)
REM Run this script to perform security checks manually

echo 🔒 Order Service Security Check
echo ================================

echo.
echo 1. 📦 Checking for outdated dependencies...
mvn versions:display-dependency-updates

echo.
echo 2. 🔍 Running OWASP Dependency Check (Critical vulnerabilities only)...
mvn org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=9 -DsuppressionsLocation=.github/dependency-check-suppressions.xml -DskipProvidedScope=true -DskipTestScope=true

echo.
echo 3. 📊 Security Report Location:
echo    HTML Report: target/dependency-check-report.html
echo    XML Report:  target/dependency-check-report.xml

echo.
echo 4. 🎯 Security Recommendations:
echo    - Review the generated reports
echo    - Update dependencies regularly
echo    - Monitor for new vulnerabilities
echo    - Run this check before production deployment

echo.
echo ✅ Security check completed!
pause