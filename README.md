# Products-api

This is application providing a REST API to manage products.

# How to run

Start application by gradle with a database(need installed Docker on your machine!)

```bash
$ ./gradlew clean dockerComposeUp
```

The application will be available on port **8080**

You can use swagger-ui by url **http://localhost:8080/swagger-ui.html** to send requests

Stop it

```bash
$ ./gradlew dockerComposeDown
```

# REST API

The REST API is described below.

## Get products

### Request

`GET /api/v1/products`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/products

### Response

    HTTP/1.1 200 OK
    transfer-encoding: chunked
    Content-Type: application/json

    []

## Create product

### Request

`POST /api/v1/products`

    curl -i -H 'Accept: application/json' -H 'Content-Type: application/json' --data-raw '{"name":"Milk","price":"1.2"}' http://localhost:8080/api/v1/products

### Response

    HTTP/1.1 201 Created
    Location: /api/v1/products/1
    Content-Type: application/json
    Content-Length: 73

    {"id":1,"name":"Milk","price":1.2,"createdAt":"2020-09-17T06:39:02.272Z"}

## Get specific product

### Request

`GET /api/v1/products/{id}`

    curl -i -H 'Accept: application/json' http://localhost:8080/api/v1/products/1

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 73

    {"id":1,"name":"Milk","price":1.2,"createdAt":"2020-09-17T06:40:21.579Z"}

## Update specific product

### Request

`PUT /api/v1/products/{id}`

    curl -i -H 'Accept: application/json' -H 'Content-Type: application/json' --data-raw '{"name":"Milk","price":"2.3"}' -X PUT http://localhost:8080/api/v1/products/1

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 73

    {"id":1,"name":"Milk","price":2.3,"createdAt":"2020-09-17T06:40:21.579Z"}

## Delete specific product

### Request

`DELETE /api/v1/products/{id}`

    curl -i -H 'Accept: application/json' -X DELETE http://localhost:8080/api/v1/products/1

### Response

    HTTP/1.1 204 No Content