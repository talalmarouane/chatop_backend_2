# Script de redémarrage intelligent pour ChâTop
Write-Host "=== Redémarrage intelligent de ChâTop ===" -ForegroundColor Cyan

$port = 3001

# 1. Vérification et libération du port
Write-Host "1. Vérification du port $port..." -ForegroundColor Yellow
$connections = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue

if ($connections) {
    Write-Host "   ⚠️ Le port $port est occupé. Nettoyage en cours..." -ForegroundColor Magenta
    $processes = $connections | Select-Object -ExpandProperty OwningProcess -Unique
    
    foreach ($pid_to_kill in $processes) {
        try {
            # Vérifier si le processus existe encore avant de le tuer
            $process = Get-Process -Id $pid_to_kill -ErrorAction SilentlyContinue
            if ($process) {
                Stop-Process -Id $pid_to_kill -Force -ErrorAction SilentlyContinue
                Write-Host "   ✓ Processus $pid_to_kill ($($process.ProcessName)) arrêté avec succès." -ForegroundColor Green
            }
        }
        catch {
            Write-Host "   ⚠️ Impossible d'arrêter le processus $pid_to_kill : $_" -ForegroundColor Red
        }
    }
    
    # Attendre un peu que le système libère le port
    Start-Sleep -Seconds 2
}
else {
    Write-Host "   ✓ Le port $port est libre." -ForegroundColor Green
}

# 2. Double vérification
$stillInUse = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
if ($stillInUse) {
    Write-Host "❌ ERREUR : Le port $port est toujours occupé. Abandon." -ForegroundColor Red
    exit 1
}

# 3. Lancer l'application
Write-Host "2. Démarrage de l'application..." -ForegroundColor Yellow
Write-Host "--------------------------------------------------" -ForegroundColor DarkGray
mvn spring-boot:run
