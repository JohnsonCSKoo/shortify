# Shortify - URL Shortener Service
Shortify is a URL shortening service built with Spring Boot and Redis. It features a caching architecture for optimal performance and scalability.  

## Features
- Create shortened (but easy to remember) URLs from long URLs
- Multi-level caching:
  - Application-level caching with Redis
  - HTTP-level caching for CDN integration
- Rate limiting with Redis and Bucket4J

## Technology Stack
### Backend
- Java
- Spring Boot
- Redis
- Postgres
- Maven

### Frontend
- React
- TypeScript

### Infrastructure
- Redis (for caching)
- CDN support
- Bucket4J (for rate limiting)

### Caching Architecture
Shortify implements a two-level caching strategy:  
1. Application-level caching with 12-hour TTL  
```
@Cacheable(value = "links", key = "#request.id")
public LinkResponse getLink(GetLinkRequest request) {
  ...
}
```

2. HTTP Cache Headers to Enables CDN and browser caching  
```
CacheControl cacheControl = CacheControl.maxAge(12, TimeUnit.HOURS)
    .noTransform()
    .cachePublic();
```

## Setup and Installation
### Prerequisites
- JDK 17+
- Maven
- Redis
- Docker
- Postgres
- Node.js

### Configuration
**Backend**  
Configure database connection in `application.yml`. Alternatively, set up the following attributes using an `.env` file:  
```
DATABASE_URL=
DATABASE_USERNAME=
DATABASE_PASSWORD=
REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
```

**Frontend**  
Configure the backend URL in `frontend/config.ts`.

### Running the Application
**Backend**  
1. Using docker-compose:
  `docker-compose up`

2. Using Maven:
  `mvn spring-boot:run`

**Frontend**
1. Navigate to project directory:
  `cd frontend`
2. Install dependencies:
  `npm install`
3. Start the development server:
  `npm run dev`


### API Endpoints
- `POST /api/v1/links/` - Create a new shortened URL
- `GET /api/v1/links/{id}` - Retrieve original URL by ID

### CDN Integration
The caching mechanism works with various CDN providers. Modify the TTL (12-hrs by default) in code if required:
```
// LinkController.java

public ResponseEntity<LinkResponse> getLink(@PathVariable String id) {
    ...
    
    CacheControl cacheControl = CacheControl.maxAge(12, TimeUnit.HOURS) // modify as required
            .noTransform()
            .cachePublic();
    ...
}
```