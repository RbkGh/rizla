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
>>> this runs all integration and unit tests in project directory

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

