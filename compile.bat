@echo off
cd src
javac -encoding UTF-8 logintestfinal/View/FilterSearch.java logintestfinal/model/*.java logintestfinal/database/*.java logintestfinal/dao/*.java
if %errorlevel% equ 0 (
    echo Compilation successful
    java logintestfinal.View.FilterSearch
) else (
    echo Compilation failed
)
pause 