# MSSC Cloud for Beer
The Spring Cloud Lab

## The MSSC Beer Service

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

#### How Many Database Connections created in DB Server
- `SELECT sum(numbackends) FROM pg_stat_database;`

### ActiveMQ-Artemis in Docker Container
- For Windows 10: 
  - ``` winpty docker run -it --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis ```
- For Linux: 
  - ``` docker run -it --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis ```

### Resources

* https://ec.haxx.se/usingcurl/usingcurl-verbose/usingcurl-writeout[cURL - Write Out]
* https://stackoverflow.com/questions/18215389/how-do-i-measure-request-and-response-times-at-once-using-curl[cURL - Measure Times]

