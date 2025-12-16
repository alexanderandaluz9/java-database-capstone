# Smart Clinic Management System – Architecture Design

## English

## Architecture Summary

The Smart Clinic Management System follows a three-tier architecture composed of Presentation, Application, and Data layers. The presentation layer supports both server-rendered HTML dashboards using Thymeleaf for admin and doctor users, and RESTful APIs for other modules such as appointments and patient records. The application layer is built with Spring Boot and contains MVC controllers, REST controllers, and a centralized service layer that handles business logic. The data layer integrates two databases: MySQL for structured relational data using Spring Data JPA, and MongoDB for flexible document-based data such as prescriptions using Spring Data MongoDB. This architecture promotes separation of concerns, scalability, and maintainability.

## Numbered Flow of Data and Control

1. A user interacts with the system either through a Thymeleaf-based web dashboard or a REST API client.
2. The request is received by the Spring Boot application and routed to the appropriate controller based on the URL and HTTP method.
3. Thymeleaf controllers handle requests for HTML views, while REST controllers handle API requests and return JSON responses.
4. The controller delegates processing to the service layer.
5. The service layer applies business rules and coordinates operations across different entities.
6. The service interacts with repositories to access data.
7. Structured data is stored or retrieved from MySQL using JPA entities, while unstructured data such as prescriptions is stored or retrieved from MongoDB using document models.
8. The retrieved data is mapped to application models.
9. The controller returns the final response to the client as either rendered HTML or JSON data.

## Spanish

# Sistema de Gestión de Clínicas SMART – Diseño de Arquitectura

## Resumen de la Arquitectura

El Sistema de Gestión de Clínicas SMART sigue una arquitectura web de tres capas: Presentación, Aplicación y Datos.  
La capa de presentación soporta tanto paneles web renderizados en el servidor mediante Thymeleaf para usuarios administradores y médicos, como APIs REST para otros módulos del sistema.  
La capa de aplicación está desarrollada con Spring Boot e incluye controladores MVC, controladores REST y una capa de servicios centralizada que contiene la lógica de negocio.  
La capa de datos integra dos bases de datos: MySQL para datos estructurados utilizando Spring Data JPA, y MongoDB para datos no estructurados y flexibles, como las recetas médicas, utilizando Spring Data MongoDB.  
Esta arquitectura permite una clara separación de responsabilidades, mejora la escalabilidad y facilita el mantenimiento del sistema.

## Flujo Numerado de Datos y Control

1. El usuario interactúa con el sistema a través de un panel web basado en Thymeleaf o mediante un cliente que consume las APIs REST.
2. La solicitud es recibida por la aplicación Spring Boot y se enruta al controlador correspondiente según la URL y el método HTTP.
3. Los controladores Thymeleaf manejan las solicitudes que devuelven vistas HTML, mientras que los controladores REST procesan solicitudes de API y devuelven respuestas en formato JSON.
4. El controlador delega el procesamiento de la solicitud a la capa de servicios.
5. La capa de servicios aplica las reglas de negocio y coordina operaciones entre distintas entidades del sistema.
6. La capa de servicios se comunica con los repositorios para acceder a los datos.
7. Los datos estructurados se almacenan o recuperan desde MySQL mediante entidades JPA, mientras que los datos no estructurados, como las recetas médicas, se gestionan en MongoDB mediante modelos de documentos.
8. Los datos obtenidos se transforman en modelos de la aplicación.
9. Finalmente, el controlador devuelve la respuesta al cliente en forma de HTML renderizado o datos JSON, según el tipo de solicitud.
