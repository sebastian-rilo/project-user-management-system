
# Project-User Management System

### Autor: Sebastian Rilo

## Tecnologies
`Spring Framework` `Java 17` `Maven` `Eclipse` `HSQLDB` `Lombok`

## Descripción
This is a small scale Spring boot application witch simulates a `Project/User` managment system where multiple users can be assigned to multiple projects.




## Important Notice

This App was made using Lombok. for proper execution Lombok should be downloaded. the steps to do this are [here](https://www.digitalocean.com/community/tutorials/java-project-lombok).


## Endpoints

These are the current available endpoints:

### Project

#### Get all items

Retrieves a paginated list of projects.

```http
  GET /api/projects
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `page` | `int` | The page indicator for the list of projects. The default value is **0** and can't be smaller than **0** |
|`size` | `int` | The page size for the list of projects. The default value is **5** and can't be smaller than **1** |

#### Get one by Id

Retrieves a single project by its Id.

```http
  GET /api/projects/${id}
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing project, otherwise the server will return an error message |

#### Get one by name

Retrieves paginated list of projects by their names matching a string.

```http
  GET /api/projects/name
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `value` | `String` | The search parameter for this endpoint. It will try to match against the project names to retrieve a list. it musn't be a blank value | 
| `page` | `int` | The page indicator for the list of projects. The default value is **0** and can't be smaller than **0** |
|`size` | `int` | The page size for the list of projects. The default value is **5** and can't be smaller than **1** |

#### Create one project

Creates a single project and saves it in the database.

```http
  POST /api/projects
```

It receives a JSON body with the following parameters

```json
{
    "name": "name of the project",
    "description": "the project description"
}
```
If the new project has the same name as another project, the server will send an error message. 


#### update one project

Gets and updates a single project in the database.

```http
  PUT /api/projects/${id}
```
| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing project, otherwise the server will return an error message |

It receives a JSON body with the following parameters

```json
{
    "name": "new name of the project",
    "description": "the new project description"
}
```
If the updated project's new name is the same as another project's, the server will send an error message. The updated project must also exist in the database. if it doesn't, or if there would be no changes to the existing database project, then the server will respond with an error message.

#### Assign user to project

Retrieves a single project by its Id and assign a user to it.

```http
  PATCH /api/projects/${id}/assign-user/${email}
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing project, otherwise the server it will return an error message |
| `email` | `String` | The assigned user's email. the email must be an existing user property value in the database. if its not, or if the user is already assigned to the project. the server will respond with an error message

#### Remove one project by Id

Removes a single project by its Id.

```http
  DELETE /api/projects/${id}
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing project, otherwise the server will return an error message |

### User

#### Get all items

Retrieves a paginated list of users.

```http
  GET /api/users
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `page` | `int` | The page indicator for the list of users. The default value is **0** and can't be smaller than **0** |
|`size` | `int` | The page size for the list of users. The default value is **5** and can't be smaller than **1** |

#### Get one by Id

Retrieves a single user by its Id.

```http
  GET /api/users/${id}
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing user, otherwise the server will return an error message |

#### Get one by name

Retrieves paginated list of users by their names matching a string.

```http
  GET /api/users/name
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `value` | `String` | The search parameter for this endpoint. It will try to match against the user names to retrieve a list. it musn't be a blank value | 
| `page` | `int` | The page indicator for the list of users. The default value is **0** and can't be smaller than **0** |
|`size` | `int` | The page size for the list of users. The default value is **5** and can't be smaller than **1** |

#### Get one by email

Retrieves a single user by its email.

```http
  GET /api/users/email
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `email` | `string` | The search parameter. Should correspond with an existing user, otherwise the server will return an error message |

#### Create one user

Creates a single user and saves it in the database.

```http
  POST /api/users
```

It receives a JSON body with the following parameters

```json
{
    "name": "name of the user",
    "description": "the user description"
}
```
If the new user has the same name as another user, the server will send an error message. 


#### update one user

Gets and updates a single user in the database.

```http
  PUT /api/users/${id}
```
| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing user, otherwise the server will return an error message |

It receives a JSON body with the following parameters

```json
{
    "name": "new name of the user",
    "email": "the new user email"
}
```
If the updated user's new name is the same as another user's, the server will send an error message. The updated user must also exist in the database. if it doesn't, or if there would be no changes to the existing database user, then the server will respond with an error message.

#### Remove one user by Id

Removes a single user by its Id.

```http
  DELETE /api/users/${id}
```

| Parameter | Type     | Description                | 
| :-------- | :------- | :------------------------- |
| `id` | `long` | The search parameter. Should correspond with an existing user, otherwise the server will return an error message |

## Testing

this Project uses the JUnit and Mockito frameworks for testing. there are currently 33 unit tests inside this app.