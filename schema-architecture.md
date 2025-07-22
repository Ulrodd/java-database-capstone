This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.


1. The user accesses a page, either a server-rendered dashboard (Admin/Doctor) or an API endpoint (Appointment, Patient).
2. The request is received by either a Thymeleaf controller (for HTML) or a REST controller (for JSON).
3. The controller delegates business logic to the service layer.
4. The service layer applies business rules and interacts with one or more repositories.
5. Repositories connect to either MySQL (structured) or MongoDB (document-based) to fetch/store data.
6. Retrieved data is bound into model objects (@Entity for MySQL, @Document for MongoDB).
7. The data is returned to the user: either rendered as an HTML page or sent back as a JSON response.
