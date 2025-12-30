# Script pour tuer le processus sur le port 3001
Write-Host "Recherche du processus sur le port 3001..." -ForegroundColor Yellow

$connections = Get-NetTCPConnection -LocalPort 3001 -ErrorAction SilentlyContinue

if ($connections) {
    $processes = $connections | Select-Object -ExpandProperty OwningProcess -Unique
    
    foreach ($pid in $processes) {
        Write-Host "✓ Arrêt du processus PID: $pid" -ForegroundColor Green
        Stop-Process -Id $pid -Force
    }
    
    Write-Host "✓ Port 3001 libéré !" -ForegroundColor Green
}
else {
    Write-Host "✓ Aucun processus trouvé sur le port 3001" -ForegroundColor Cyan
}
