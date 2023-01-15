# topjava-22-graduation Restaurants


This is a graduation project for the [Topjava](https://topjava.ru/topjava) course.

##  Technical requirement
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

---
The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

---
Run: `mvn spring-boot:run` in a root directory.

  [REST API documentation](http://localhost:8080/swagger-ui.html)  
Credentials:
```
Admin: admin@gmail.com / admin
User:  user@yandex.ru / password
Guest: guest@gmail.com / guest
```
---
The most popular technologies/ tools/ frameworks used in project:

- JDK 17 
- Maven
- SpringBoot 3.0/Security/Data JPA
- Hibernate ORM
- JUnit 5/AssertJ
- Lombok
- H2
- Caffeine Cache
- SpringDoc/Swagger/OpenAPI 3.0
---
