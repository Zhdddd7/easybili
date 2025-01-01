# User API Documentation

## Base URL
`/user`

---
### **GET** `/user/{id}`

Retrieve detailed information about a user by their ID.

#### Request
- **Path Parameter**:
    - `id` (String): The ID of the user.

#### Response
- **Success (200)**:
  ```json
  {
      "status": "success",
      "message": "Operation successful",
      "data": {
          "id": "string",
          "name": "string",
          "email": "string",
          "joinTime": "string (ISO 8601 format)"
      }
  }
  ```
----
### **POST** `/user`

Create a new user with the provided details.

### Request
- **Body**:
  ```json
  {
      "name": "string",
      "email": "string",
      "password": "string"
     }
  ```
#### Response
- **Success (200)**:
```json
  {
  "status": "success",
  "message": "User created successfully",
  "data": "string (Confirmation message)"
}
  ```
----
### **PUT** `/user`

Update an existing user's information.

#### Request
**Body**:
  ```json
  {
    "id": "string",
    "name": "string",
    "email": "string"
}
  ```
#### Response
- **Success (200)**:
```json
  {
  "status": "success",
  "message": "User updated successfully",
  "data": "string (Confirmation message)"
}
  ```
----
### **DELETE** `/user{id}`

Delete a user by their ID.

#### Request
**Path Parameter**:
- `id` (String): The ID of the user.
#### Response
- **Success (200)**:
```json
{
  "status": "success",
  "message": "User deleted successfully",
  "data": "string (Confirmation message)"
}
  ```

    