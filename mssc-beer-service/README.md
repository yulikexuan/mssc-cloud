# MSSC Beer Service

## Calling API

### Get Beer By Id with Inventory Data
curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/beer/026cc3c8-3a0c-4083-a05b-e908048c1b08?showInventoryOnHand=true

### Get Beer By Id without Inventory Data
curl -s -w '\n\nTotal: %{time_total} Seconds\n' http://localhost:8081/api/v1/beer/026cc3c8-3a0c-4083-a05b-e908048c1b08

## Resources
- [cURL - Write Out] - https://ec.haxx.se/usingcurl/usingcurl-verbose/usingcurl-writeout
- [cURL - Measure Times] - https://stackoverflow.com/questions/18215389/how-do-i-measure-request-and-response-times-at-once-using-curl
