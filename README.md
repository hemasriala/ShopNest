# ShopNest - Spring Boot E-Commerce App

Tech: Java 17, Spring Boot 3, Thymeleaf, Spring Data JPA, MySQL, Lombok.
Runs at http://localhost:8081 and connects to MySQL on port 3307.

## Run steps
1. Start MySQL (port 3307).
2. Open the `.env` file and fill DB_USERNAME and DB_PASSWORD.
3. Open the project in IntelliJ (File > Open > select the ShopNest folder, trust it, wait for Maven to download).
4. Enable Lombok annotation processing if asked (Settings > Build > Compiler > Annotation Processors).
5. Run `ShopNestApplication.java`.
6. Open http://localhost:8081

The database `shopnest_db` and all tables are created automatically, and 16 sample products are added on the first run.
