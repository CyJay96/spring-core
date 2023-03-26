## **Gift Certificate API**

### **Task**

**Business requirements**

1. Develop web service for Gift Certificates system:
   - CreateDate, LastUpdateDate - format ISO 8601. Example: 2018-08-29T06:12:15.156.
   - Duration - in days (expiration period).

2. The system should expose REST APIs to perform the following operations:
   - CRUD operations for GiftCertificate. If new tags are passed during creation/modification –  
     they should be created in the DB. For update operation - update only fields, that pass in request,  
     others should not be updated. Batch insert is out of scope.
   - CRUD operations for Tags.
   - Get certificates with tags (all params are optional and can be used in conjunction):
     1. by tag name (ONE tag);
     2. search by part of name/description (can be implemented, using DB function call);
     3. sort by date or by name ASC/DESC (extra task: implement ability to apply both sort type at the same time).

**Application requirements**

1. JDK version: 17 – use Streams, java.time.*, etc. where it is possible.
2. Application packages root: ru.clevertec.ecl.
3. Any widely-used connection pool could be used.
4. Spring JDBC Template should be used for data access.
5. Use transactions where it’s necessary.
6. Java Code Convention is mandatory (exception: margin size – 120 chars).
7. Build tool: Gradle, latest version.
8. Web server: Apache Tomcat.
9. Application container: Spring IoC. Spring Framework, the latest version.
10. Database: PostgreSQL, latest version.
11. Testing: JUnit 5.+, Mockito.
12. Service layer should be covered with unit tests not less than 80%.
13. Repository layer should be tested using integration tests with an in-memory embedded database (all operations with certificates).
14. As a mapper use Mapstruct.
15. Use lombok.

**General requirements**

1. Code should be clean and should not contain any "developer-purpose" constructions.
2. App should be designed and written with respect to OOD and SOLID principles.
3. Code should contain valuable comments where appropriate.
4. Public APIs should be documented (Javadoc).
5. Clear layered structure should be used with responsibilities of each application layer defined.
6. JSON should be used as a format of client-server communication messages.
7. Convenient error/exception handling mechanism should be implemented: all errors should be meaningful on backend side. Example: handle 404 error:  
HTTP Status: 404  
response body  
{  
“errorMessage”: “Requested resource not found (id = 55)”,  
“errorCode”: 40401  
}  
where *errorCode” is your custom code (it can be based on http status and requested resource - certificate or tag).
8. Abstraction should be used everywhere to avoid code duplication.
9. Several configurations should be implemented (at least two - dev and prod).

**Application restrictions**

It is forbidden to use:
1. Spring Boot.
2. Spring Data Repositories.
3. JPA.


### **The stack of technologies used**

**API Technologies:**
- SOLID
- OOP
- DI
- REST

**Backend technologies:**
- Java 17
- Spring Framework 6.0.7:
  - Spring WebMVC
  - Spring JDBC
- Lombok
- MapStruct

**Build Tool:**
- Gradle

**DataBase:**
- PostgreSQL
- H2
- LiquiBase

**Testing:**
- Junit 5
- Mockito

### **Endpoints**

**Gift Certificate**

| **HTTP METHOD** |            **ENDPOINT**             |            **DECRIPTION**             |
|:---------------:|:-----------------------------------:|:-------------------------------------:|
|    **POST**     |     `/api/v0/giftCertificates`      |      Create new Gift Certificate      |
|     **GET**     |     `/api/v0/giftCertificates`      |       Get all Gift Certificates       |
|     **GET**     | `/api/v0/giftCertificates/criteria` | Get all Gift Certificates by criteria |
|     **GET**     |   `/api/v0/giftCertificates/{id}`   |      Get Gift Certificate by ID       |
|     **PUT**     |   `/api/v0/giftCertificates/{id}`   |     Update Gift Certificate by ID     |
|    **PATCH**    |   `/api/v0/giftCertificates/{id}`   | Partial Update Gift Certificate by ID |
|   **DELETE**    |   `/api/v0/giftCertificates/{id}`   |     Delete Gift Certificate by ID     |

**Tag**

| **HTTP METHOD** |    **ENDPOINT**     |      **DECRIPTION**      |
|:---------------:|:-------------------:|:------------------------:|
|    **POST**     |   `/api/v0/tags`    |      Create new Tag      |
|     **GET**     |   `/api/v0/tags`    |       Get all Tags       |
|     **GET**     | `/api/v0/tags/{id}` |      Get Tag by ID       |
|     **PUT**     | `/api/v0/tags/{id}` |     Update Tag by ID     |
|    **PATCH**    | `/api/v0/tags/{id}` | Partial Update Tag by ID |
|   **DELETE**    | `/api/v0/tags/{id}` |     Delete Tag by ID     |
