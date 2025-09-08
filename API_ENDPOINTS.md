## API Endpoints Reference

This document consolidates the HTTP API endpoints defined by the microservices in this repository. Use the API Gateway base URL when available (default: http://localhost:8084). Each service also runs on its own port for local debugging (see `docker-compose.yml`).

---

### Quick notes
- Gateway base URL: `http://localhost:8084` (see `api-gateway`)
- Service discovery (Eureka) runs at `http://localhost:8761`.
- Many services include security (Okta) — requests may require authentication.
- These endpoints were extracted from the services' `openapi.yaml` files: `sb-blog-info`, `sb-blog-business`, `sb-blog-comments`.

---

## sb-blog-info (Blog Info Service)
- Default service port: 7070
- Base paths in OpenAPI: `/blogs/v1/info` and `/blogs/v1`

- GET /blogs/v1/info/
  - Summary: Retrieve all blogs
  - Response: 200 — array of BlogResponse

- GET /blogs/v1/info/search
  - Summary: Search blogs
  - Query parameters:
    - `query` (string, optional)
    - `sortBy` (string, optional)
    - `size` (integer, required)
    - `page` (integer, required)
    - `sortDir` (string, required)
  - Response: 200 — object (search result)

- GET /blogs/v1/info/user/{userId}
  - Summary: Retrieve blogs by userId
  - Path parameters: `userId` (string, required)
  - Response: 200 — array of BlogResponse; 404 if not found

- GET /blogs/v1/{blogId}
  - Summary: Retrieve a blog by its ID
  - Path parameters: `blogId` (string, required)
  - Response: 200 — BlogResponse; 404 if not found

Schema highlights (sb-blog-info)
- BlogResponse: { id, title, userId, content, categories[], tags[], comments[] }
- CommentRes: { id, userId, blogId, content }
- APIError: { code, message }

---

## sb-blog-business (Blog Business Service)
- Default service port: 7071
- Base path in OpenAPI: `/blogs/v1/business`

- POST /blogs/v1/business/
  - Summary: Add a new blog
  - Request body: BlogReq (userId, title, content, categories[], tags[])
  - Response: 201 — BlogRes
  - Errors: 400, default error

- PUT /blogs/v1/business/{blogId}
  - Summary: Update an existing blog
  - Path parameters: `blogId` (string, required)
  - Request body: BlogReq
  - Response: 200 — BlogRes; 400/404/default

- DELETE /blogs/v1/business/{blogId}
  - Summary: Delete a blog
  - Response: 204 — no content; 404 if not found

- POST /blogs/v1/business/{blogId}/comments
  - Summary: Create a new comment for a blog
  - Path parameters: `blogId` (string, required)
  - Request body: CommentRequest
  - Response: 201 — CommentResponse; 400/404/default possible

Schema highlights (sb-blog-business)
- BlogReq (required: userId, title, content)
- BlogRes: { id, userId, title, content, categories[], tags[], comments[] }
- CommentRequest / CommentResponse: { id?, userId, blogId, content }
- ErrorResponse: { code, message }

---

## sb-blog-comments (Comments Service)
- Default service port: 7075
- Base path in OpenAPI: `/comments/v1`

- POST /comments/v1/
  - Summary: Create a new comment
  - Request body: CommentRequest
  - Response: 201 — CommentResponse

- GET /comments/v1/
  - Summary: Retrieve all comments
  - Response: 200 — array of CommentResponse

- GET /comments/v1/blog/{blogId}
  - Summary: Retrieve comments for a specific blog
  - Path parameters: `blogId` (string, required)
  - Response: 200 — array of CommentResponse

- GET /comments/v1/search
  - Summary: Search comments
  - Query parameters: `query` (RSQL), `sortBy`, `size`, `page`, `sortDir`
  - Response: 200 — object (search result)

- GET /comments/v1/{commentId}
  - Summary: Retrieve a comment by ID
  - Path parameters: `commentId` (string, required)
  - Response: 200 — CommentResponse; 404 if not found

- PUT /comments/v1/{commentId}
  - Summary: Update a comment by ID
  - Request body: CommentRequest
  - Response: 200 — CommentResponse; 404 if not found

- DELETE /comments/v1/{commentId}
  - Summary: Delete a comment by ID
  - Response: 204 — no content; 404 if not found

Schema highlights (sb-blog-comments)
- CommentRequest: { id?, userId, blogId, content }
- CommentResponse: { id, userId, blogId, content }
- ErrorResponse: { code, message }

---

## Common notes and usage
- To call the APIs through the gateway use the gateway base URL and the paths above, e.g. `GET http://localhost:8084/blogs/v1/info/`.
- If you need to call services directly for debugging, use the service ports from the `docker-compose.yml` file.
- Authentication: many services include Spring Security / Okta starter. Provide proper OAuth/OIDC credentials or disable security for local testing.
- Swagger UI: the project README indicates a Swagger UI is available at `http://localhost:8084/swagger-ui/index.html` when the gateway exposes documentation.

---

If you want, I can:
- generate a more detailed markdown with request/response examples for each endpoint, or
- add per-endpoint curl examples (with and without auth), or
- combine all service OpenAPI files into a single `openapi-combined.yaml`.

Requirements coverage
- Create consolidated markdown listing all API endpoints — Done
