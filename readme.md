```markdown
# KDT_HACK API Documentation

## Base URL
`/api`

---

## Authentication Endpoints

### POST `/auth/signup`
Register a new user.

**Request Body** (`application/json`):
```json
{
  "name": "string (2-100 chars, letters/spaces/hyphens only)",
  "lastname": "string (2-100 chars, same pattern)",
  "username": "string (unique)",
  "birthday": "YYYY-MM-DD",
  "email": "string (unique)",
  "password": "string (8-100 chars)"
}
```

**Responses:**
- `201 Created` – `{ "message": "успешная регистрация" }`
- `400 Bad Request` – Email/username exists or validation error.

### POST `/auth/signin`
Authenticate and receive a JWT token.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
- `200 OK` – `{ "token": "jwt-token-string" }`
- `401 Unauthorized` – Invalid credentials or account blocked/deleted.

---

## User Profile Endpoints
All endpoints under `/user/{id}` require authentication. `{id}` is the user's UUID.

### GET `/user/{id}`
Get user details by ID.  
**Roles:** `USER`, `ADMIN`  
*Users can only access their own profile; ADMIN can access any.*

**Response:** `200 OK` – `User` object.

### POST `/user/{id}`
Update user profile.  
**Roles:** `USER` (own profile only)  
**Request Body:** `UpdateUserRequest` (includes name, lastName, username, birthday, avatar, email, phone, telegram, github, job, level, skills array).

**Responses:**
- `200 OK` – Updated `User`.
- `400 Bad Request` – Username already taken.
- `404 Not Found` – User not found.

### POST `/user/{id}/change-password`
Change password for the authenticated user.  
**Roles:** `USER` (own profile only)  

**Request Body:**
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

**Responses:**
- `200 OK` – `{ "message": "Password changed successfully" }`
- `400 Bad Request` – Old password incorrect.

---

## Admin Endpoints
Base path: `/api/admin`  
**All endpoints require `ADMIN` role.**

### Skills Management

#### POST `/admin/add-skills`
Add new skills.  
**Request Body:** Array of `Skills` objects (e.g., `[{"id":1,"name":"Java"}]`).  
**Response:** `200 OK` – `{ "message": "Skills added successfully" }`

#### DELETE `/admin/delete-skills`
Delete skills by name.  
**Request Body:** Same array format; only `name` is required.  
**Responses:**
- `200 OK` – Success message.
- `404 Not Found` – Some skills not found.

### User Management

#### GET `/admin/getall-users`
Retrieve all users.  
**Response:** `200 OK` – List of `User` objects.

#### POST `/admin/update-user/{id}`
Update any user (admin override).  
**Path Variable:** `id` (UUID)  
**Request Body:** `UpdateUserRequest`  
**Response:** `200 OK` – Updated `User`.

#### POST `/admin/block-user/{id}`
Set user status to `BLOCKED`.  
**Response:** `200 OK` – Updated `User`.

#### POST `/admin/delete-user/{id}`
Soft delete (status `DELETED`).  
**Response:** `200 OK` – Updated `User`.

### Points & Achievements

#### POST `/admin/add-points/{id}`
Add points to a user; achievements are awarded automatically.  
**Path Variable:** `id` (UUID)  
**Request Body:** Integer `points`.  
**Response:** `200 OK` – Updated `User`.

### Schedule Management (Admin only)

#### POST `/admin/schedule/create-event`
Create a new event.  
**Request Body:** `Schedule` record:
```json
{
  "name": "Event name",
  "briefDescription": "Brief",
  "description": "Full description",
  "skills": [ /* skill objects */ ],
  "startDateTime": "2025-05-01T10:00:00",
  "endDateTime": "2025-05-03T18:00:00"
}
```
**Response:** `201 Created` – Created `Schedule`.

#### PUT `/admin/schedule/update-event/{id}`
Update an event.  
**Path Variable:** `id` (event ID)  
**Request Body:** `Schedule` record.  
**Response:** `200 OK` – Updated `Schedule`.

#### DELETE `/admin/schedule/delete-event/{id}`
Delete an event.  
**Response:** `200 OK` (empty body).

---

## Public Schedule Endpoints
Base path: `/api/schedule`  
*No authentication required.*

### GET `/schedule` or `/schedule/admin`
Get all events.  
**Response:** `200 OK` – List of `Schedule` records.

### GET `/schedule/{id}`
Get a single event by ID.  
**Response:** `200 OK` – `Schedule` object; `404` if not found.

---

## Event Registration Endpoints
Base path: `/api/events`

### POST `/events/{eventId}/register-solo`
Register authenticated user as solo participant.  
**Roles:** `USER`  
**Path Variable:** `eventId`  
**Response:** `200 OK` – `{ "message": "Успешная регистрация" }`  
`400 Bad Request` – Already registered.

### GET `/events/{eventId}/registrations`
Get all registrations for an event.  
**Roles:** `ADMIN`, `JUDGE`  
**Response:** `200 OK` – List of `EventRegistrationDto`.

---

## Data Models (Partial)

### Enums
- **UserStatus:** `ACTIVE`, `BLOCKED`, `DELETED`
- **UserRole:** `USER`, `ADMIN`, `JUDGE`, `PARTNER`
- **UserJob:** `FRONT`, `BACK`, `DESIGNER`, `PROJECT`, `GAME`
- **UserLevel:** `BEGINNER`, `INTERMEDIATE`, `ADVANCED`
- **RegistrationType:** `SINGLE`, `TEAM`
- **RegistrationStatus:** `ACTIVE`, `CANCELLED`

---

## Authentication
All protected endpoints require a JWT token in the `Authorization` header:  
`Authorization: Bearer <token>`

Roles are internally prefixed with `ROLE_` (e.g., `ROLE_ADMIN`).

---

## Error Responses
Standard format:
```json
{ "message": "Error description" }
```

