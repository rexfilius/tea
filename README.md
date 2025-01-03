# Tea API

This is a RESTful API built using Spring Boot for a Blog Application that supports both mobile and web apps. The API provides endpoints for user authentication, blog posts, comments, and categories with role-based security. The project also includes live Swagger documentation to facilitate API usage.

## Features

### Authentication and Authorization
- **User Registration:** Users can sign up and choose one of two roles: `admin` or `user`. A verification code is
also sent to user's email to verify account.
- **User Login:** Users can log in to receive a JWT access token for subsequent API calls.
- **Role-Based Security:** Admin and user roles are implemented to restrict access to specific endpoints.

### Post Management
- Create a post
- Get a single post
- Get all posts
- Update a post
- Delete a post
- Get all posts under a category

### Comment Management
- Create a comment
- Get a single comment
- Get all comments
- Update a comment
- Delete a comment

### Category Management
- Create a category (admin only)
- Get a single category
- Get all categories
- Update a category (admin only)
- Delete a category (admin only)
 
### Entity Relationships
- A category can have many posts.
- A post can belong to one category.
- A post can have many comments.

### Security
- **Database Authentication:** User credentials are stored securely in a database.
- **JWT Authentication:** All API calls are secured using JWT tokens.
- **Role-Based Access Control:** Admin-exclusive endpoints are restricted.

[//]: # (### Additional Features)

[//]: # (- **Live Swagger Documentation:** You can access the swagger documentation for this api here [docs]&#40;http://tea-rest-api-env.eba-grfhpa2h.us-east-1.elasticbeanstalk.com/swagger-ui/index.html&#41;)

## API Endpoints

### Authentication
- `POST /v1/auth/register` - Register a new user.
- `POST /v1/auth/login` - Log in and receive a JWT token.
- `POST /v1/auth/verify` - Verify a new user.
- `POST /v1/auth/resend/{email}` - Resend verification code.

### Posts
- `POST /v1/posts` - Create a new post.
- `GET /v1/posts/{id}` - Retrieve a single post.
- `GET /v1/posts` - Retrieve all posts.
- `PUT /v1/posts/{id}` - Update a post.
- `DELETE /v1/posts/{id}` - Delete a post.
- `GET /v1/posts/category/{id}` - Get all posts under a category.

### Comments
- `POST /v1/posts/{postId}/comments` - Create a new comment.
- `GET /v1/posts/{postId}/comments/{commentId}` - Get a comment under post.
- `GET /v1/posts/{postId}/comments` - Get all comments under a post.
- `PUT /v1/posts/{postId}/comments/{commentId}` - Update a comment.
- `DELETE /v1/posts/{postId}/comments/{commentId}` - Delete a comment.

### Categories
- `POST /v1/categories` - Create a new category (admin only).
- `GET /v1/categories/{id}` - Retrieve a single category.
- `GET /v1/categories` - Retrieve all categories.
- `PUT /v1/categories/{id}` - Update a category (admin only).
- `DELETE /v1/categories/{id}` - Delete a category (admin only).

