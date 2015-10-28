#!/usr/bin/env bash
echo "Build all services..."
#Maven rausgenommen
echo "Verzeichnis wechseln..."
cd movie-database-shop-app
echo "NPM-Install start..."
sudo npm install
echo "Bower-Install start..."
bower install --allow-root
echo "GRUNT start..."
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
