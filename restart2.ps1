Write-Host "Killing processes on port 4200..."
$processes = Get-NetTCPConnection -LocalPort 4200 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
if ($processes) {
    foreach ($p in $processes) {
        Stop-Process -Id $p -Force -ErrorAction SilentlyContinue
        Write-Host "Killed process with PID: $p"
    }
}
else {
    Write-Host "No process found on port 4200."
}
Write-Host "Done."
