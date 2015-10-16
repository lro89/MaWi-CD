#!/usr/bin/env bash
echo "Build all services..."
#Maven rausgenommen
sudo apt-get install nodejs-legacy
cd movie-database-shop-app
sudo rm -rf node_modules
sudo npm install
bower install --allow-root
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
sudo docker-compose build
