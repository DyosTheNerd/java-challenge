### Implementation of Employee Rest API

What has been done:
- pagination of employee list
    - currently just a simple pagination model
- Integrated Unittests
    - Controller tests for API end points
    - Service Layer Tests
- Introduced usage of DTOs to prevent users from setting IDs or thinking they can set IDs
    - to improve required and necessary data objects at the api level
- Implemented Optimistic Locking
    - Update and Delete now require the current Entity Version
- Introduced Error Handling controller
    - ensure no stack trace leaks to client
    - give a consistent json responsee
    - 1 generic exception and 2 use case specific onces added: ResourceNotFound and Locking Exception
- Implemented ServiceLayer Caching on Get, with evictions for update and delete
    - depending on the usecase, this might also be done on dao layer. 
    - configuration can be improved.
- configured a simple api basic auth based configuration (admin / password) 
- added a postman script v2.1 
    - projectdir/postman/
    
- Added docuementation on SL and controller


What could be improved?
- Implement duplication check for new entity
    - duplicate records are a huge problem, but also need business requirements. 
        - Is it possible to have the same name in the department? Add birth date etc. 
        - would also need to be applied for update cases
- proper security implementation
    - integrate a user db and their passwords
    - remove h2 access
- use a different db
    - h2 in memory is nice for development, but insufficient for most production usecases. 
- hateoas implementation to have an explorable api.

- more testing: added a small postman collection, but more system tests could be done.



- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.

#### experience in Java
- MVC based Architectures in Java for 6 Years with:
    - Spring specifically for about 2 years with:
        - Spring boot 1 Year with:
            - Grails 6 months 

