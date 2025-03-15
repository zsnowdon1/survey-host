# Survey Host Service

## Overview
The Survey Host Service is a backend service responsible for managing surveys within the Voting App. It provides endpoints to create, retrieve, update, delete, and toggle the status of surveys.

## Technologies Used
- **Spring Boot** (REST API)
- **PostgreSQL** (Database)
- **Spring Data JPA** (Database interaction)
- **SLF4J & Logback** (Logging)
- **CORS Enabled** for cross-origin requests

## API Endpoints
### Base URL: `/api/host/surveys`

#### 1. Create a Survey
**Endpoint:** `POST /api/host/surveys`
- **Request Body:** `SurveyDTO`
- **Response:** `SurveyDTO`
- **Description:** Creates a new survey.

#### 2. Retrieve Surveys
**Endpoint:** `GET /api/host/surveys`
- **Response:** `List<SurveyDTO>`
- **Description:** Fetches all surveys.

#### 3. Get Survey Details
**Endpoint:** `GET /api/host/surveys/{id}`
- **Response:** `SurveyDetailDTO`
- **Description:** Retrieves details of a specific survey.

#### 4. Delete a Survey
**Endpoint:** `DELETE /api/host/surveys/{id}`
- **Response:** `DeleteSurveyRespons
