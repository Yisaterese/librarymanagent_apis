# Library Management API

This project is a Library Management API built to manage books, users, and borrowing functionality in a library system. The API provides endpoints for adding, reading, updating, and deleting books, as well as user management and book borrowing features.

## Features

- Add, retrieve, update, and delete books.
- User registration and role assignment.
- Borrow and return books.
- Retrieve user and borrower details.
- Secure authentication and authorization using JWT.

## Technologies Used

- **Java 17**: Core programming language.
- **Spring Boot**: Framework for building the REST API.
- **PostgreSQL**: Database for persisting data.
- **Docker**: Containerization for easy deployment.
- **Cloudinary**: Image storage for book cover uploads.
- **Lombok**: To reduce boilerplate code.
- **GitHub**: Version control system.
- **JWT (JSON Web Token)**: For secure authentication and authorization.

## API Endpoints

### Book Endpoints
**POST** `/api/books/add_book` - Add a new book.
**GET** `/api/books/{id}` - Get book details by ID.
**GET** `/api/books/{isbn}` - Get book details by ISBN.
**GET** `/api/books/all_books` - Retrieve all books.
**PATCH** `/api/books/{id}` - Update book details.
**DELETE** `/api/books/{id}` - Delete a book by ID.

### User Endpoints
**POST** `/api/users/register` - Register a new user.
**POST** `/api/users/assign-role/{userId}` - Assign a role to a user.
**GET** `/api/users/{userId}` - Get user details by ID.
**GET** `/api/users` - Retrieve all users.
**PUT** `/api/users/update/{userId}` - Update user details.
**DELETE** `/api/users/delete_book/delete/{userId}` - Delete a user by ID.
**POST** `/api/users/borrow/{userId}/{bookId}` - Borrow a book for a user.
**POST** `/api/users/return/{userId}/{bookId}` - Return a borrowed book.
**GET** `/api/users/borrower/{logId}` - Retrieve details of a book borrower.

## Getting Started

To get started with this project, clone the repository and follow the instructions below.

### Prerequisites

- Java 17 or higher
- Maven
- Docker
- Cloudinary Account (for cover image uploads)

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/Yisaterese/librarymanagement_api.git
   cd librarymanagement_api

2. Build the project:
   mvn clean install

3. Run the application:
   mvn spring-boot:run

## Authorization

This application uses JWT (JSON Web Token) for authentication and authorization. 
Ensure you have valid tokens for accessing secured endpoints.

## Dockerization
The project is containerized for easy deployment. To build and run the Docker container:

1. Build the Docker image:
    docker build -t library-management-api .

2. Run the Docker container:
   docker run -p 8080:8080 library-management-api
