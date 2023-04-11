## **Gift Certificate API**

### **The stack of technologies used**

**API Technologies:**
- SOLID
- OOP
- DI
- REST

**Backend technologies:**
- Java 17
- Spring Boot 3
- Lombok
- MapStruct

**Build Tool:**
- Gradle 7.5

**DataBase:**
- PostgreSQL
- H2
- LiquiBase

**Testing:**
- Junit 5
- Mockito

### **Endpoints**

**Gift Certificate**

| **HTTP METHOD** |                         **ENDPOINT**                          |            **DESCRIPTION**            |
|:---------------:|:-------------------------------------------------------------:|:-------------------------------------:|
|    **POST**     |                  `/api/v0/giftCertificates`                   |      Create new Gift Certificate      |
|     **GET**     |                  `/api/v0/giftCertificates`                   |       Get all Gift Certificates       |
|     **GET**     |              `/api/v0/giftCertificates/criteria`              | Get all Gift Certificates by criteria |
|     **GET**     |                `/api/v0/giftCertificates/{id}`                |      Get Gift Certificate by ID       |
|     **PUT**     |                `/api/v0/giftCertificates/{id}`                |     Update Gift Certificate by ID     |
|    **PATCH**    |                `/api/v0/giftCertificates/{id}`                | Partial Update Gift Certificate by ID |
|    **PATCH**    |  `/api/v0/giftCertificates/add/{giftCertificateId}/{tagId}`   |      Add Tag to Gift Certificate      |
|    **PATCH**    | `/api/v0/giftCertificates/delete/{giftCertificateId}/{tagId}` |   Delete Tag from Gift Certificate    |
|   **DELETE**    |                `/api/v0/giftCertificates/{id}`                |     Delete Gift Certificate by ID     |

**Tag**

| **HTTP METHOD** |    **ENDPOINT**     |      **DECRIPTION**      |
|:---------------:|:-------------------:|:------------------------:|
|    **POST**     |   `/api/v0/tags`    |      Create new Tag      |
|     **GET**     |   `/api/v0/tags`    |       Get all Tags       |
|     **GET**     | `/api/v0/tags/{id}` |      Get Tag by ID       |
|     **PUT**     | `/api/v0/tags/{id}` |     Update Tag by ID     |
|    **PATCH**    | `/api/v0/tags/{id}` | Partial Update Tag by ID |
|   **DELETE**    | `/api/v0/tags/{id}` |     Delete Tag by ID     |

**User**

| **HTTP METHOD** |           **ENDPOINT**           |         **DESCRIPTION**          |
|:---------------:|:--------------------------------:|:--------------------------------:|
|     **GET**     |         `/api/v0/users`          |          Get all Users           |
|     **GET**     |       `/api/v0/users/{id}`       |          Get User by ID          |
|     **GET**     | `/api/v0/users/highestOrderCost` | Get User with highest order cost |

**Order**

| **HTTP METHOD** |                 **ENDPOINT**                  |      **DESCRIPTION**      |
|:---------------:|:---------------------------------------------:|:-------------------------:|
|    **POST**     | `/api/v0/orders/{userId}/{giftCertificateId}` |     Create new Order      |
|     **GET**     |               `/api/v0/orders`                |      Get all Orders       |
|     **GET**     |      `/api/v0/orders//byUserId/{userId}`      |   Get Orders by User ID   |
|     **GET**     |             `/api/v0/orders/{id}`             |      Get Order by ID      |
|     **GET**     |     `/api/v0/orders//{orderId}/{userId}`      | Get Order by ID & User ID |
