# ============================================
# Script rapide : Utiliser JDK 23 (déjà installé)
# ============================================

Write-Host "=== Configuration Maven pour utiliser JDK 23 ===" -ForegroundColor Cyan
Write-Host ""

$jdk23Home = "C:\Program Files\Java\jdk-23"

if (-not (Test-Path $jdk23Home)) {
    Write-Host "✗ JDK 23 introuvable dans : $jdk23Home" -ForegroundColor Red
    exit 1
}

Write-Host "✓ JDK 23 trouvé : $jdk23Home" -ForegroundColor Green

# Créer le dossier .mvn
$mvnDir = Join-Path $PSScriptRoot ".mvn"
if (-not (Test-Path $mvnDir)) {
    New-Item -ItemType Directory -Path $mvnDir -Force | Out-Null
}

# Créer jvm.config
$jvmConfigPath = Join-Path $mvnDir "jvm.config"
$javaHomePath = $jdk23Home.Replace('\', '/')
Set-Content -Path $jvmConfigPath -Value "-Djava.home=$javaHomePath" -Encoding UTF8

Write-Host "✓ Configuration appliquée" -ForegroundColor Green
Write-Host ""
Write-Host "Maven utilisera maintenant JDK 23 pour ce projet !" -ForegroundColor Yellow
Write-Host ""
Write-Host "Lance maintenant : mvn clean spring-boot:run" -ForegroundColor White
