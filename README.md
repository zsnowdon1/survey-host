# Survey Host Service

## Overview
The Survey Host Service is a backend service responsible for managing surveys within the Voting App.

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
- **Description:** Retrieves all surveys.

#### 3. Toggle Survey Status
**Endpoint:** `PUT /api/host/surveys/{surveyId}/toggle`
- **Response:** `ToggleStatusResponse`
- **Description:** Toggles the status of a survey between active and inactive.

#### 4. Delete a Survey
**Endpoint:** `DELETE /api/host/surveys/{surveyId}`
- **Response:** `DeleteSurveyResponse`
- **Description:** Deletes a specific survey.

## Live Vote Tracking
### Base URL: `/api/host/surveys/live`

#### 1. Subscribe to Live Vote Updates
**Endpoint:** `GET /api/host/surveys/live/{surveyId}`
- **Response:** `SSE Stream`
- **Description:** Establishes a connection for real-time vote updates using Server-Sent Events (SSE).

#### 2. Retrieve Live Survey Results
**Endpoint:** `GET /api/host/surveys/{surveyId}/results`
- **Response:** `GetSurveyResultsResponse`
- **Description:** Retrieves the latest results for a given survey.
