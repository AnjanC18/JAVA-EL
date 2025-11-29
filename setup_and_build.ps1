$ErrorActionPreference = "Stop"

$mavenVersion = "3.9.6"
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$installDir = "c:\Users\anjan\.gemini\antigravity\scratch\dairy-management\tools"
$mavenHome = "$installDir\apache-maven-$mavenVersion"
$mavenBin = "$mavenHome\bin"

# Create tools directory
if (-not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Force -Path $installDir | Out-Null
}

# Download Maven if not exists
$zipPath = "$installDir\maven.zip"
if (-not (Test-Path $mavenHome)) {
    Write-Host "Downloading Maven $mavenVersion..."
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath
    
    Write-Host "Extracting Maven..."
    Expand-Archive -Path $zipPath -DestinationPath $installDir -Force
    
    Remove-Item $zipPath
}

# Set environment variables for this session
$env:JAVA_HOME = "D:\Downloads\jdk-25.0.1"
$env:PATH = "$mavenBin;$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Maven setup complete."
Write-Host "Java Home: $env:JAVA_HOME"
Write-Host "Maven Home: $mavenHome"

# Verify installation
mvn -version

# Run build
Write-Host "Building project..."
mvn clean install -DskipTests
