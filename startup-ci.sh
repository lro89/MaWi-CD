# Startup-Scrit für CI-Server
#Docker Start
sudo service cgconfig restart
sudo nohup /home/ec2-user/docker-latest daemon

# ELK-Stack Start
cd /home/ec2-user/elk-stack/mvdb-docker-elk/
git pull
docker-compose up -d
