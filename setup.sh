#!/usr/bin/env sh
# One-time install of the game jar into the local Maven repository.
set -e
cd "$(dirname "$0")"
mvn install:install-file -Dfile=lib/Space-Invaders.jar -DgroupId=org.example -DartifactId=Space-Invaders-Original -Dversion=1.0-SNAPSHOT -Dpackaging=jar
