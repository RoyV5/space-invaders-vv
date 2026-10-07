# One-time install of the game jar into the local Maven repository.
Set-Location $PSScriptRoot
mvn install:install-file "-Dfile=lib/Space-Invaders.jar" "-DgroupId=org.example" "-DartifactId=Space-Invaders-Original" "-Dversion=1.0-SNAPSHOT" "-Dpackaging=jar"
