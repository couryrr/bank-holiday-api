# bank-holiday-api

The following instances are needed:

Redis
```
docker run --name bankholiday_redis -p 16379:6379 -d --rm redis:6.2-alpine
```
MySql (running on port 13306)
```
docker run --name bankholiday_mysql -e MYSQL_DATABASE=bank_holiday -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d --rm -p 13306:3306 --rm mysql:8.0
```
Rabbit MQ
```
docker run -d --rm -p 25672:5672 -p 35672:15672 --name bankholiday_rabbitmq rabbitmq:3-management
```
Application can be run with
```
./mvnw clean package
./mvnw spring-boot:run
```

A docker-compose file is set up too.
```
docker-compose build 
docker-compose up -d

sh ./docker-dev.sh # This will build and restart
                  # app in docker
```
## Access the API

Using a client like curl or postman call http://localhost:8080/bank-holiday-api/fetch-data 
to seed the database or wait for a 10-minute mark.

## Upcoming additions add
- Understand SpringBootTest better
- Look up 
  - Canadian Holidays
  - European Holidays
- GitHub Actions
- Setup env for server

## Lessons Learned
- When using docker the port that the docker container uses is the actual port not the forwarded
    - 13306:3306 
    - MySql workbench uses 13306 on my local
    - Spring boot application in docker uses 3306 for the connection string
- Redis cache set up annotation is per method so the cache name should be unique
- Seems like all of spring boots applications get written in the compose or docker file.
- Mockito mock is not used to create dummy data :( cannot find a good way to do that yet.
- Remember to set the hostname for connection strings as the docker container name.

## Observations

### Testing
This is not something I have had to set up before. Usually in existing projects 
or established companies the how of testing has already been established. Usually
that existing test suite is complicated based on the fact that no time is dedicated
to that. Developers just know that we have to test our code and that code coverage
has to be > 70...80%.

With this project I have been able to take time to really understand what I am trying
to test. There are ways to make sure your code is testable and to make sure your
tests are fast.

#### My goal for testing
The idea is to have sufficient self checks to cover good and bad cases where the ROI
of the test suite does not outweigh its impact on code.

#### My terms for testing

**Unit Test** - A test for method logic within a class. This can be done my simply
creating an instance of a class, the expected input and comparing/asserting the 
output.

In my case I have even created "entity" objects outside a repo because I was
not testing the "integration" to a repo.

```
var input = ...
var sut = new Class();
sut.someAction(input);
//Assertion logic
```

**Internal Integration Test** - This is testing two classes working together and making
sure the contract for that integrations inputs and outputs are upheld. Currently,
I think it should not be systems (caches, databases). If that is followed then 
these tests can be just as fast as unit tests.

The main lesson learned here is that instead of relying on dependency injection in the 
application context of a test. Adding a constructor with the attributes *not injected*
allowed mocks to be passed in for the repo. This way only logic for the integration
between two classes is being tested. The application did not need to boot and mocks
sufficed.

The same could be done for external api. Taking a snapshot of known good request and
response can serve as the mocks. The only application I can control is my own. So I
should only really test the code I can change.

**External Integration Test** - This would be to a database, api and other external systems
to the current system under test. Application context would be important. I still need to
explore this.

**End-to-end** (e2e) - This would be from the client/customers point of view. When called
the full system is tested. Application context, APIs, caching, databases, files, emails, etc. 
This could be from the UI with a tool like cypress or even JUnit calling curl. Nothing is 
mocked, but the environment can be spun up and thrown away. This may not live in the project,
but there are advantages to having tests like this close to code.





