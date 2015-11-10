# Installation Docker in Version 1.9 (binary) und Docker-Compose
# Infrastruktur für T-Stage und P-Stage
sudo yum update -y 
wget https://get.docker.com/builds/Linux/x86_64/docker-latest
chmod +x docker-latest
sudo  killall ./docker-latest
sudo  killall docker
sudo service cgconfig restart
sudo nohup ./docker-latest daemon
sudo pip install -U docker-compose
