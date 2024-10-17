# Library Application API

## Overview

This is a RESTful API for managing books in a library. The API provides functionalities to:
- Retrieve book information by ISBN.
- Find books by author name.
- Add new books.
- Remove books by ISBN.
- Increment or decrement the available copies of a book.

The API follows RESTful principles and returns appropriate HTTP status codes for success and failure scenarios.

## Endpoints

### 1. **Find a Book by ISBN**

**Endpoint:** `GET /books/{isbn}`  
**Description:** Retrieves a book's details using its ISBN number.

- **Path Parameter:**
    - `isbn` (String) - The ISBN number of the book.

- **Response:**
    - **200 OK** - Returns the `BookDTO` object with details of the book.
    - **404 Not Found** - If no book with the given ISBN is found.

#### Example Request:
```http
GET /books/978-3-16-148410-0
```

#### Example Response:
```json
{
  "isbn": "978-3-16-148410-0",
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "publicationYear": 1925,
  "availableCopies": 12
}
```

---

### 2. **Find Books by Author Name**

**Endpoint:** `GET /api/books`  
**Description:** Retrieves a list of books by the specified author.

- **Query Parameter:**
    - `authorName` (String) - The name of the author.

- **Response:**
    - **200 OK** - Returns a list of `BookDTO` objects by the author.
    - **404 Not Found** - If no books by the given author are found.

#### Example Request:
```http
GET /api/books?authorName=F.%20Scott%20Fitzgerald
```

#### Example Response:
```json
[
  {
    "isbn": "978-3-16-148410-0",
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "publicationYear": 1925,
    "availableCopies": 12
  }
]
```

---

### 3. **Remove a Book by ISBN**

**Endpoint:** `DELETE /api/books/{isbn}`  
**Description:** Deletes a book from the library's collection by ISBN.

- **Path Parameter:**
    - `isbn` (String) - The ISBN number of the book.

- **Response:**
    - **204 No Content** - If the book is successfully deleted.
    - **404 Not Found** - If no book with the given ISBN is found.

#### Example Request:
```http
DELETE /api/books/978-3-16-148410-0
```

---

### 4. **Create a New Book**

**Endpoint:** `POST /api/books`  
**Description:** Adds a new book to the library.

- **Request Body:**
    - `BookResource` - A JSON object containing details of the new book.

- **Response:**
    - **201 Created** - Returns the created `BookDTO` object.
    - **400 Bad Request** - If the request body is invalid.

#### Example Request:
```json
POST /api/books
{
  "title": "The Nizams of Hyderabad",
  "author": "Adam Harper",
  "publicationYear": 1960,
  "availableCopies": 5
}
```

#### Example Response:
```json
{
  "isbn": "978-0-7432-7356-5",
  "title": "The Nizams of Hyderabad",
  "author": "Adam Harper",
  "publicationYear": 1960,
  "availableCopies": 5
}
```

---

### 5. **Increment Available Copies of a Book**

**Endpoint:** `PATCH /api/books/{isbn}/available-copies/increment`  
**Description:** Increases the number of available copies for a book by 1.

- **Path Parameter:**
    - `isbn` (String) - The ISBN number of the book.

- **Response:**
    - **200 OK** - Returns the updated `BookDTO` object with incremented available copies.
    - **404 Not Found** - If no book with the given ISBN is found.

#### Example Request:
```http
PATCH /api/books/978-3-16-148410-0/available-copies/increment
```

#### Example Response:
```json
{
  "isbn": "978-3-16-148410-0",
  "title": "The Nizams of Hyderabad",
  "author": "F. Scott Fitzgerald",
  "publicationYear": 1925,
  "availableCopies": 13
}
```

---

### 6. **Decrement Available Copies of a Book**

**Endpoint:** `PATCH /api/books/{isbn}/available-copies/decrement`  
**Description:** Decreases the number of available copies for a book by 1.

- **Path Parameter:**
    - `isbn` (String) - The ISBN number of the book.

- **Response:**
    - **200 OK** - Returns the updated `BookDTO` object with decremented available copies.
    - **400 Bad Request** - If decrementing would result in negative available copies.
    - **404 Not Found** - If no book with the given ISBN is found.

#### Example Request:
```http
PATCH /api/books/978-3-16-148410-0/available-copies/decrement
```

#### Example Response:
```json
{
  "isbn": "978-3-16-148410-0",
  "title": "The Nizams of Hyderabad",
  "author": "F. Scott Fitzgerald",
  "publicationYear": 1925,
  "availableCopies": 11
}
```

---

## Data Models

### BookDTO
Represents the data returned for a book.

```json
{
  "isbn": "string",
  "title": "string",
  "author": "string",
  "publicationYear": "int",
  "availableCopies": "int"
}
```

### BookResource
Represents the data required to create a new book.

```json
{
  "isbn": "string",
  "title": "string",
  "author": "string",
  "publicationYear": "int",
  "availableCopies": "int"
}
```

---

## Error Handling
- **404 Not Found**: If a book with the specified `isbn` or `authorName` is not found.
- **400 Bad Request**: If invalid input is provided, such as an invalid request body during book creation.
- **204 No Content**: If a delete request is successful.

--- 

## Getting Started

1. Clone the repository.
2. Ensure you have a valid database connection set up in your `application.properties` or `application.yml`.
3. Run the application using a Java IDE or build tool like Maven or Gradle.

