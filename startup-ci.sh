# Startup-Scrit für CI-Server
#Docker Start
sudo service cgconfig restart
sudo nohup ./docker-latest daemon

# ELK-Stack Start
cd elk-stack/mvdb-docker-elk/
git pull
docker-compose up -d
