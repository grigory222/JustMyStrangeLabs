# Compile
```bash
javac -d bin -sourcepath src -cp lib/Pokemon.jar src/**/*.java src/*.java
cd bin
touch manifest.txt
echo 'Main-Class: Main
Class-Path: ../lib/Pokemon.jar' >> Manifest.txt 
jar cvfm My.jar Manifest.txt .
java -jar My.jar
```
