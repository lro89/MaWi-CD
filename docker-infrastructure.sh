# Installation Docker in Version 1.9 (binary) und Docker-Compose
# Infrastruktur für T-Stage und P-Stage
sudo yum update -y 
wget https://get.docker.com/builds/Linux/x86_64/docker-latest
chmod +x docker-latest
sudo service cgconfig restart
sudo ./docker-latest -d &
sudo pip install -U docker-compose
