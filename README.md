Project Evolution

This project began as a tightly coupled User Service that managed user-related activities such as:

    User signup

    User lookup

Over time, the service evolved with the following enhancements:

    Improved display messages – made responses more user-friendly.

    Unit tests – added to improve reliability and catch issues early.

    Authentication – implemented JWT (JSON Web Token) so that users receive a token after login, which they can use to access protected endpoints such as delete, update, find, etc.

    Caching with Redis – query results (e.g., findById) were cached to reduce repeated calls to the MySQL database and improve performance.

    RabbitMQ integration – the User Service (as a producer) sent user details to RabbitMQ, which stored the data in a queue and forwarded it to the Email Service when ready.

Identified Problem

Despite these improvements, the application remained tightly coupled. This meant changes in one component could still impact others, limiting scalability and flexibility.
Moving Toward a Loosely Coupled Architecture

To address this, the application was restructured into smaller, independent services:

    UUID Service – handles unique ID generation separately.

    Email Service – receives data from RabbitMQ and sends emails to notify user signup without blocking the User Service.

    SMS Service – receives data from RabbitMQ and sends SMS notifications. The User Service sends the necessary data to RabbitMQ, which then forwards it to the SMS Service asynchronously.

This evolution demonstrates the application’s journey from a monolithic, tightly coupled design to a distributed, loosely coupled microservices architecture.
Added API Gateway and Security Verification

    Implemented an API Gateway to route requests to services using their service names, allowing access via their respective endpoints.

    Integrated security verification to validate the JWT token issued by the User Service before granting access to other services.

    Designed the architecture so that if additional services are introduced, filters can be added at the gateway level to enforce token verification from the User Service before processing requests.
