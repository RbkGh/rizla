# Rizla - Organizational Transport Booker

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

## Requirements
```textmate
1. Java 17+
```

## Run
> 1. Build application
```shell
./mvnw clean package -DskipTests
```

> 2. Start docker container
```shell
docker-compose up --build
```

> Application is now live at  http://localhost:4444/ and postgres db at port 9999

## Run Tests- run in root directory:
```shell
./mvnw clean
```
```shell
./mvnw test
```
>>> this runs all integration and unit tests in project directory, 100% class coverage!

>> To delete db data :
```jshelllanguage
docker-compose down -v
```
## Development Setup 
> docker-compose.dev.yml file allows hot reload using spring boot devtools, 
> the dev setup allows easy development and deployment in a containerized environ
```shell
docker-compose -f docker-compose.dev.yml up --build
```
> dev url for app will be live at http://localhost:4444/ and postgres db at port 9999

# Create Account
### Request
```shell
POST http://localhost:4444/api/public/users/register
Accept: application/json
Content-Type: application/json
# userType can be either PASSENGER,EXEC_PASSENGER or DRIVER

{
  "username": "kofi_passenger@yahoo.com",
  "name": "Passenger Kofi",
  "password": "1234",
  "userType": "PASSENGER"
}
```
#### Response
```text
HTTP/1.1 201 
Location: http://localhost:4444/api/public/users/register/7
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 28 Aug 2024 20:06:35 GMT

{
  "id": 7,
  "username": "kofi_passenger@yahoo.com",
  "name": "Passenger Kofi"
}
Response file saved.
> 2024-08-28T200635.201.json

```

# Login To Get JWT Token
### Request 
```shell
POST http://localhost:4444/api/public/auth/login
Accept: application/json
Content-Type: application/json
# userType can be either PASSENGER,EXEC_PASSENGER or DRIVER

{
  "username": "kofi_passenger@yahoo.com",
  "password": "1234"
}
```
### Response 
```shell
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 28 Aug 2024 20:13:24 GMT

{
  "jwtToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrb2ZpX3Bhc3NlbmdlckB5YWhvby5jb20iLCJpYXQiOjE3MjQ4NzYwMDQsImV4cCI6MTcyNTIzNjAwNH0.g2qo2eosXt65ZGIqCz0IFO1eQRWgB_EU_szA4ScDo3IMzLwSJIswf5ESz8UkqJqIuEKCHJJfvLfRHMjkydj6Mw"
}
Response file saved.
> 2024-08-28T201324.200.json
```
# Get Cars Available For Booking 
> Some cars are already created for booking 
### Request 

# Book Available Slots For Vehicle 
```text
Available slots are one of the following :
HOUR_1,ie (7am-7:59am)
HOUR_2,ie (8am-8:59am)
HOUR_3,ie (9am-9:59am)
HOUR_4,ie (10am-10:59am)
HOUR_5,ie (11am-11:59am)
HOUR_6,ie (12pm-12:59pm)
HOUR_7,ie (12pm-12:59pm)
HOUR_8,ie (12pm-12:59pm)
any option aside these treats it as default choice of HOUR_1
```

### Request
```shell
GET http://localhost:4444//api/vehicles
Accept: application/json
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrb2ZpX3Bhc3NlbmdlckB5YWhvby5jb20iLCJpYXQiOjE3MjQ4NzYwMDQsImV4cCI6MTcyNTIzNjAwNH0.g2qo2eosXt65ZGIqCz0IFO1eQRWgB_EU_szA4ScDo3IMzLwSJIswf5ESz8UkqJqIuEKCHJJfvLfRHMjkydj6Mw

```

### Response
```shell
GET http://localhost:4444/api/vehicles

HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 28 Aug 2024 20:20:55 GMT

[
  {
    "id": 1,
    "passengerID": null,
    "vehicleID": null,
    "vehicleManufacturer": "Toyota",
    "vehicleModel": "Camry",
    "vehicleLicenseNumber": "GT453",
    "bookingRequestTime": null,
    "bookingConfirmedTime": null,
    "bookingStartTime": null,
    "bookingEndTime": null
  },
  {
    "id": 2,
    "passengerID": null,
    "vehicleID": null,
    "vehicleManufacturer": "Mercedes Benz",
    "vehicleModel": "C45",
    "vehicleLicenseNumber": "GT454",
    "bookingRequestTime": null,
    "bookingConfirmedTime": null,
    "bookingStartTime": null,
    "bookingEndTime": null
  },
  {
    "id": 3,
    "passengerID": null,
    "vehicleID": null,
    "vehicleManufacturer": "Tesla",
    "vehicleModel": "Model S Plaid",
    "vehicleLicenseNumber": "GT455",
    "bookingRequestTime": null,
    "bookingConfirmedTime": null,
    "bookingStartTime": null,
    "bookingEndTime": null
  },
  {
    "id": 4,
    "passengerID": null,
    "vehicleID": null,
    "vehicleManufacturer": "Honda",
    "vehicleModel": "Civic",
    "vehicleLicenseNumber": "GT456",
    "bookingRequestTime": null,
    "bookingConfirmedTime": null,
    "bookingStartTime": null,
    "bookingEndTime": null
  },
  {
    "id": 5,
    "passengerID": null,
    "vehicleID": null,
    "vehicleManufacturer": "Hyundai",
    "vehicleModel": "Elantra",
    "vehicleLicenseNumber": "GT457",
    "bookingRequestTime": null,
    "bookingConfirmedTime": null,
    "bookingStartTime": null,
    "bookingEndTime": null
  },
  {
    "id": 6,
    "passengerID": null,
    "vehicleID": null,
    "vehicleManufacturer": "Nissan",
    "vehicleModel": "Almera",
    "vehicleLicenseNumber": "GT458",
    "bookingRequestTime": null,
    "bookingConfirmedTime": null,
    "bookingStartTime": null,
    "bookingEndTime": null
  }
]
Response file saved.
> 2024-08-28T202055.200.json

Response code: 200; Time: 92ms (92 ms); Content length: 1396 bytes (1.4 kB)

```

# Book Car As Passenger or Executive Passenger 

### Request 
> format: /api/bookings/{vehicleID}/car/{passengerID}/user

```shell
POST http://localhost:4444/api/bookings/2/car/7/user
Accept: application/json
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrb2ZpX3Bhc3NlbmdlckB5YWhvby5jb20iLCJpYXQiOjE3MjQ4NzYwMDQsImV4cCI6MTcyNTIzNjAwNH0.g2qo2eosXt65ZGIqCz0IFO1eQRWgB_EU_szA4ScDo3IMzLwSJIswf5ESz8UkqJqIuEKCHJJfvLfRHMjkydj6Mw

{
  "timeSlot": "HOUR_1"
}

```

### Response
```shell
POST http://localhost:4444/api/bookings/2/car/7/user
Accept: application/json
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrb2ZpX3Bhc3NlbmdlckB5YWhvby5jb20iLCJpYXQiOjE3MjQ4NzYwMDQsImV4cCI6MTcyNTIzNjAwNH0.g2qo2eosXt65ZGIqCz0IFO1eQRWgB_EU_szA4ScDo3IMzLwSJIswf5ESz8UkqJqIuEKCHJJfvLfRHMjkydj6Mw

{
  "timeSlot": "HOUR_1"
}

<> 2024-08-28T203207.201.json

```

# Confirm Booking 
> /api/bookings/{vehicleBookingID}/confirm
> Only a user with Driver role can confirm a booking by passenger so we need to create a driver user and use their jwt for this endpoint

### Request 
```shell
PUT http://localhost:4444/api/bookings/17/confirm
Accept: application/json
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkcml2ZXJfcm9kbmV5QGdtYWlsLmNvbSIsImlhdCI6MTcyNDg3ODM0MiwiZXhwIjoxNzI1MjM4MzQyfQ.uHDfGCC5oAX-IGubsf_Z26PyL4pFwaaMOJ-G-mmrHEd47mpuxKJPO0k2XopogwHHSDWKKBKtbkn4qEIDic2zXA

```
### Response
```shell
PUT http://localhost:4444/api/bookings/17/confirm

HTTP/1.1 204 
Date: Wed, 28 Aug 2024 20:57:46 GMT

<Response body is empty>
```

### we follow the same process to make for /{vehicleBookingID}/cancel and /executive/{vehicleBookingID}



