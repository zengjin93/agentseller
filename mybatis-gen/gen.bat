@ECHO OFF
del /f /s /q ..\app\dal\src\main\resources\META-INF\mappers\auto
md ..\app\dal\src\main\resources\META-INF\mappers\auto
java -cp .;mybatis-generator-core-1.3.3-SNAPSHOT-baifubao.jar org.mybatis.generator.api.ShellRunner -configfile config.xml -overwrite
pause
@ECHO ON
