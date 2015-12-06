#!/usr/bin/env bash
# Startup-Scrit für CI-Server
#Docker Start
sudo service cgconfig restart
sudo nohup /home/ec2-user/docker-latest daemon &

echo "Docker Daemon gestartet"

# ELK-Stack Start
cd /home/ec2-user/elk-stack/mvdb-docker-elk/
git pull
su - ec2-user -c  "cd /home/ec2-user/elk-stack/mvdb-docker-elk/;docker-compose up"

echo "ELK-Stack gestartet"

# Start Nexus


# SonarQube Container starten
echo "Starte SonarQube..."
sudo /home/ec2-user/docker-latest run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube:5.1
