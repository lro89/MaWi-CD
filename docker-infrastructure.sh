# Installation Docker in Version 1.7.2 und Docker-Compose
# Infrastruktur für T-Stage und P-Stage
sudo yum update -y 
sudo yum install -y docker 
sudo service docker start 
sudo usermod -a -G docker ec2-user 
sudo pip install -U docker-compose
