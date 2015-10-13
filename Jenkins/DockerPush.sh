#!/usr/bin/env bash

docker login
#TODO CREDENTIALS

docker tag moviedatabase_shopgui cdzwei/mvdb_shopgui:latest
docker tag moviedatabase_shop cdzwei/mvdb_shop:latest
docker tag moviedatabase_movies cdzwei/mvdb_movies:latest
docker tag moviedatabase_actors cdzwei/mvdb_actors:latest
docker tag moviedatabase_navigation cdzwei/mvdb_navigation:latest
docker tag moviedatabase_monitoring cdzwei/mvdb_monitoring:latest

docker push docker.io/cdzwei/mvdb_shopgui
docker push docker.io/cdzwei/mvdb_shop
docker push docker.io/cdzwei/mvdb_movies
docker push docker.io/cdzwei/mvdb_actors
docker push docker.io/cdzwei/mvdb_navigation
docker push docker.io/cdzwei/mvdb_monitoring


