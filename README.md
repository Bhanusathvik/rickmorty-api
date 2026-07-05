# Rick and Morty API Clone
Spring Boot + MongoDB Atlas backend for Characters, Episodes, and Locations, with JWT-secured CRUD, search, filtering, sorting, and pagination.

## Stack
- Java 17, Spring Boot 3.3
- Spring Web, Spring Data MongoDB, Spring Security, JWT (jjwt)
- MongoDB Atlas (cloud) — no local DB install needed

## Project layout (MVC)
```
model/       Character, Episode, Location, User  -> also used directly as request/response bodies
repository/  Spring Data MongoDB repositories
service/     Business logic, search/filter/sort/pagination queries
controller/  REST endpoints
security/    JwtUtil, JwtAuthFilter
config/      SecurityConfig (filter chain, password encoding, user lookup)
exception/   ApiExceptions + GlobalExceptionHandler
dto/         PagedResponse, AuthResponse
```

## 1. Set up MongoDB Atlas
1. Create a free cluster at https://www.mongodb.com/cloud/atlas
2. Create a database user and allow your IP (or `0.0.0.0/0` for testing)
3. Grab your connection string, e.g.
   `mongodb+srv://<username>:<password>@<cluster-url>/rickmorty?retryWrites=true&w=majority`

## 2. Configure environment variables
```
$env:MONGODB_URI="mongodb+srv://bhanusathvik16_db_user:<Db password >@cluster0.gwqii3u.mongodb.net/rickmorty?appName=Cluster0"
$env:JWT_SECRET="mN0yAUEYhv8MF0w8dEBX74i36CUs5q3tsyi/C8NONjs="
```
## 3. Run it
```
mvn spring-boot:run
```
App starts on `http://localhost:8080`.

## API Reference
```
> $body = @{ username = "rick"; password = "wubbalubba" } | ConvertTo-Json
>> $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method Post -ContentType "application/json" -Body $body
>> $response
username token                                                                                                                               
-------- -----                                                                                                                               
rick     eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaWNrIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTc4MzI2MzM2MiwiZXhwIjoxNzgzMzQ5NzYyfQ.9n4L6sn7LVtNo...

> $token = $response.token
>> 
>> $char = @{ name = "Rick Sanchez"; status = "Alive"; species = "Human" } | ConvertTo-Json
>> Invoke-RestMethod -Uri "http://localhost:8080/api/characters" -Method Post -ContentType "application/json" -Headers @{ Authorization = "Bearer $token" } -Body $char
id         : 6a4a70b45791b9049d3c9c5d
name       : Rick Sanchez
status     : Alive
species    : Human
episodeIds : {}
createdAt  : 2026-07-05T14:56:52.999239200Z
updatedAt  : 2026-07-05T14:56:52.999239200Z
> Invoke-RestMethod -Uri "http://localhost:8080/api/characters?status=Alive&sortBy=name&direction=asc&page=0&size=10" -Method Get -Headers @{ Authorization = "Bearer $token" }
content       : {@{id=6a4a70b45791b9049d3c9c5d; name=Rick Sanchez; status=Alive; species=Human; episodeIds=System.Object[]; 
                createdAt=2026-07-05T14:56:52.999Z; updatedAt=2026-07-05T14:56:52.999Z}}
page          : 0
size          : 10
totalElements : 1
totalPages    : 1
last          : True


> try {
>>     Invoke-RestMethod -Uri "http://localhost:8080/api/characters" -Method Get
>> } catch {
>>     "Blocked as expected: $($_.Exception.Message)"
>> }
Blocked as expected: The remote server returned an error: (403) Forbidden.
```