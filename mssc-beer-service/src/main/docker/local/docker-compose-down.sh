docker-compose down -v --remove-orphans 
docker rm -f "$(docker ps -a -q)"
docker volume rm "$(docker volume ls -q)"