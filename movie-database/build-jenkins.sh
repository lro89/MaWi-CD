#!/usr/bin/env bash
echo "Build all services..."
#Maven rausgenommen
cd movie-database-shop-app
sudo rm -rf node_modules
sudo npm install -g grunt-cli
sudo npm install grunt
sudo npm install -g bower
sudo npm install
sudo bower --allow-root install
sudo grunt
mkdir opt
cd opt
mkdir moviedatabase
cd moviedatabase
mkdir shop-app
cd ..
sudo chmod 777 moviedatabase
cd ..
rm -rf /opt/moviedatabase/shop-app
sudo cp  -R dist/. opt/moviedatabase/shop-app/
cd ..
echo "Docker-Compose build..."
sudo docker-compose build
