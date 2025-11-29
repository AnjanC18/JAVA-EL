$ErrorActionPreference = "Stop"

$mavenVersion = "3.9.6"
$installDir = "c:\Users\anjan\.gemini\antigravity\scratch\dairy-management\tools"
$mavenHome = "$installDir\apache-maven-$mavenVersion"
$mavenBin = "$mavenHome\bin"

# Set environment variables for this session
$env:JAVA_HOME = "D:\Downloads\jdk-25.0.1"
$env:PATH = "$mavenBin;$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Starting Dairy Management System..."
# Run the packaged jar directly
java -jar target/dairy-management-0.0.1-SNAPSHOT.jar
