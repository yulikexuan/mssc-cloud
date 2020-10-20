# MSSC Cloud for Beer
The Spring Cloud Lab

## The MSSC Brewery Services

### Microservice List

- beer-service
  - port: 8081

- beer-inventory-service
  - port: 8083

- beer-order-service
  - port: 8082

- beer-inventory-failover
  - port: 8084

### Spring Cloud Service List

- mssc-brewery-gateway
  - port: 9090

- mssc-brewery-eureka
  - port: 8761

- mssc-brewery-config
  - port: 8888
  
  
### The API

#### Get Beer By Id with Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/beer/026cc3c8-3a0c-4083-a05b-e908048c1b08?showInventoryOnHand=true

#### Get Beer By UPC with Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/upcbeer/0631234200036?showInventoryOnHand=true

#### Get Beer By Id without Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/beer/026cc3c8-3a0c-4083-a05b-e908048c1b08

#### Get Beer By UPC without Inventory Data

curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/upcbeer/0071990316006

### The Database

- psql -h localhost -U postgres

- How Many Database Connections created in DB Server
    - `SELECT sum(numbackends) FROM pg_stat_database;`

- List all users
    - ` \du `

- Connect to database with psql
    - ` psql -h localhost -U username databasename `

- List all database
    - ` \l `

- Connect to a specific database
    - ` \c <datebase_name> `

- List all schemas
    - ` \dn `

- List all tables
    - ` \dt `
    - ` \dt+ `
- Get detailed information on a table
    - ` \d+ <table name> `

- To quit from psql
    - ` postgres=# \q `

### ActiveMQ-Artemis in Docker Container

- For Windows 10: 
  - Start ActiveMQ-Artemis
    - ``` winpty docker run -it --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis ```
  - Stop ActiveMQ-Artemis
    - ``` docker stop <container name> ```

- For Linux: 
    - Start ActiveMQ-Artemis
      - ``` docker run -it --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis ```
  - Stop ActiveMQ-Artemis
    - ``` docker stop <container name> ```

### Solution for Tricky Issues

#### Hibernate could not initialize proxy – no Session


### Resources
* [psql cheatsheet](https://www.postgresqltutorial.com/postgresql-cheat-sheet/)
* [cURL - Write Out](https://ec.haxx.se/usingcurl/usingcurl-verbose/usingcurl-writeout)
* [cURL - Measure Times](https://stackoverflow.com/questions/18215389/how-do-i-measure-request-and-response-times-at-once-using-curl)
* [Docker-for-Windows & Hyper-V excluding but not using important port ranges](https://github.com/docker/for-win/issues/3171)
* [Hands-On Guide to Spring Cloud Contract](https://learning.oreilly.com/videos/hands-on-guide-to/9780135598436)