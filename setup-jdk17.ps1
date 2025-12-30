# ============================================
# Script d'installation et configuration JDK 17
# ============================================

Write-Host "=== Installation JDK 17 pour ChâTop ===" -ForegroundColor Cyan
Write-Host ""

# Vérifier si JDK 17 existe déjà
$jdk17Paths = @(
    "C:\Program Files\Java\jdk-17",
    "C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot",
    "C:\Program Files\Eclipse Adoptium\jdk-17*",
    "C:\Program Files\Java\jdk-17*"
)

$existingJdk17 = $null
foreach ($path in $jdk17Paths) {
    $found = Get-Item $path -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($found) {
        $existingJdk17 = $found.FullName
        break
    }
}

if ($existingJdk17) {
    Write-Host "✓ JDK 17 trouvé : $existingJdk17" -ForegroundColor Green
    $jdk17Home = $existingJdk17
} else {
    Write-Host "✗ JDK 17 non trouvé. Téléchargement en cours..." -ForegroundColor Yellow
    
    # URL de téléchargement Eclipse Temurin JDK 17 LTS (Windows x64)
    $jdkUrl = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11.msi"
    $installerPath = "$env:TEMP\temurin-jdk17.msi"
    
    Write-Host "Téléchargement depuis : $jdkUrl" -ForegroundColor Gray
    
    try {
        # Télécharger le JDK
        Invoke-WebRequest -Uri $jdkUrl -OutFile $installerPath -UseBasicParsing
        Write-Host "✓ Téléchargement terminé" -ForegroundColor Green
        
        # Installation silencieuse
        Write-Host "Installation en cours (cela peut prendre quelques minutes)..." -ForegroundColor Yellow
        Start-Process msiexec.exe -ArgumentList "/i `"$installerPath`" /quiet /norestart ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome INSTALLDIR=`"C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot\`"" -Wait -NoNewWindow
        
        # Vérifier l'installation
        $jdk17Home = "C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot"
        if (Test-Path $jdk17Home) {
            Write-Host "✓ JDK 17 installé avec succès dans : $jdk17Home" -ForegroundColor Green
        } else {
            Write-Host "✗ Erreur : Installation échouée" -ForegroundColor Red
            exit 1
        }
        
        # Nettoyer
        Remove-Item $installerPath -Force -ErrorAction SilentlyContinue
        
    } catch {
        Write-Host "✗ Erreur lors du téléchargement/installation : $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Solution manuelle :" -ForegroundColor Yellow
        Write-Host "1. Télécharge JDK 17 depuis : https://adoptium.net/temurin/releases/?version=17"
        Write-Host "2. Installe-le"
        Write-Host "3. Relance ce script"
        exit 1
    }
}

Write-Host ""
Write-Host "=== Configuration du projet pour utiliser JDK 17 ===" -ForegroundColor Cyan

# Créer le dossier .mvn s'il n'existe pas
$mvnDir = Join-Path $PSScriptRoot ".mvn"
if (-not (Test-Path $mvnDir)) {
    New-Item -ItemType Directory -Path $mvnDir -Force | Out-Null
    Write-Host "✓ Dossier .mvn créé" -ForegroundColor Green
}

# Créer le fichier jvm.config
$jvmConfigPath = Join-Path $mvnDir "jvm.config"
$javaHomePath = $jdk17Home.Replace('\', '/')
Set-Content -Path $jvmConfigPath -Value "-Djava.home=$javaHomePath" -Encoding UTF8
Write-Host "✓ Fichier .mvn/jvm.config créé" -ForegroundColor Green

# Vérifier que ça fonctionne
Write-Host ""
Write-Host "=== Vérification ===" -ForegroundColor Cyan
& "$jdk17Home\bin\java.exe" -version

Write-Host ""
Write-Host "=== Configuration terminée ! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Tu peux maintenant lancer :" -ForegroundColor Yellow
Write-Host "  mvn clean spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "Le projet utilisera automatiquement JDK 17 grâce au fichier .mvn/jvm.config" -ForegroundColor Gray
