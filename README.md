
# Expense Tracker API

This is a solution for the Blog API project on 
[https://roadmap.sh/projects/expense-tracker-api](https://roadmap.sh/projects/expense-tracker-api)

### Requirements:
- Java 25

### How to use:
This is a Spring Boot Application with a maven wrapper
so you can easily run it with **./mvnw spring-boot:run** 

#### Endpoints (Assuming localhost:8080):
- **POST localhost:8080/register** send a JSON object with a username and password and get a JWT response
- **POST localhost:8080/login** send a JSON object with a username and password and get a JWT response
- **POST localhost:8080/expenses** send a JSON object with a category and price with a JWT auth header to create an expense
- **PUT localhost:8080/expenses/{id}** send a JSON object with a category and price with a JWT auth header to update an expense
- **GET localhost:8080/expenses** to get a list of expenses affiliated with your account
- **GET localhost:8080/expenses/{id}** to get a specific expense affiliated with your account
- **DELETE localhost:8080/expenses/{id}** to delete an expense affiliated with your account
