#!/usr/bin/env bash
# Startup-Scrit für CI-Server
#Docker Start
sudo service cgconfig restart
sudo nohup /home/ec2-user/docker-latest daemon &

echo "Docker Daemon gestartet"

# ELK-Stack Start
cd /home/ec2-user/elk-stack/mvdb-docker-elk/
git pull
docker-compose up -d

echo "ELK-Stack gestartet"

# SonarQube Container starten
echo "Starte SonarQube..."
sudo /home/ec2-user/docker-latest start sonarqube
