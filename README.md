# kclk

based on 
https://stackoverflow.com/questions/55955926/how-to-reduce-the-size-of-the-access-refresh-tokens-in-keycloak

goals:
1. open source software product to allow single sign-on with Identity and Access Management aimed at modern applications and services via JWT access tokens
2. based on Keycloak API thus very limited
3. thus with small (up to 255 chars) JWT access tokens only

## how to
to build - `mvn clean install` 
to run  - run kclk-main project

## usage
#### create realm (considering run locally on port 48080)
`curl --location --request POST 'http://localhost:48080/auth/admin/realms' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "diia"
}'`

get realm - same with GET
#### create client:
`curl --location --request POST 'http://localhost:48080/auth/admin/realms/diia/clients' \
--header 'Content-Type: application/json' \
--data-raw '{
    "clientId": "diia2",    
    "attributes": {
        "access.token.lifespan": 2592000
    }
}'`
#### get client's secret: 
`curl --location --request GET 'http://localhost:48080/auth/admin/realms/diia/clients/fd385b81-8a42-4b1e-96cb-8fa0628b708f/client-secret'

#### generate token:

`curl --location --request POST 'http://localhost:48080/auth/realms/admin/protocol/openid-connect/token' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode 'grant_type=client_credentials' \
 --data-urlencode 'client_secret=password' \
 --data-urlencode 'client_id=admin'`
 
`

#### check token / get user info

`curl --location --request POST 'http://localhost:48080/auth/realms/admin/protocol/openid-connect/userinfo' \
 --header 'Authorization: Bearer eyJraWQiOiJlMTUwMWVlYS1iOWVjLTQxOWUtYmZjYy1jMTNkMGJiNTgwNjYiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTU4MTcyNTl9.YLSBeVct7-lKIod-4Qs9pVdVgMIOV3zMJulbPDfvawY'`