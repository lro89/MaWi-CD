#!/usr/bin/env bash
# Startup-Scrit für CI-Server
#Docker Start
sudo service cgconfig restart
sudo nohup /home/ec2-user/docker-latest daemon &

echo "Docker Daemon gestartet"

