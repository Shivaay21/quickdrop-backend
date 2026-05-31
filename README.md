# QuickDrop 🔒

### Privacy-First Temporary File Sharing Platform

🚀 **Live Demo:** http://54.79.58.55

---

## Overview

QuickDrop is a temporary file-sharing platform built with Java and Spring Boot that allows users to securely share files using short links.

Users can:

- Upload files instantly
- Generate shareable short URLs
- Set link expiry times
- Enable one-time downloads
- Share files through QR codes
- Download files without creating an account

The goal of QuickDrop is to provide a lightweight alternative to traditional file-sharing platforms while giving users more control over file access.

---

## Features

### File Upload
- Upload files through a simple web interface
- Stores file metadata in PostgreSQL
- Generates unique short links

### Expiring Links
Choose how long the file should remain accessible:

- 10 Minutes
- 1 Hour
- 24 Hours

Expired files automatically become inaccessible.

### One-Time Downloads
Files can be configured to be deleted after the first successful download.

### QR Code Sharing
Generate QR codes for every uploaded file so users can quickly access download links on mobile devices.

### File Preview Page
Each file gets a dedicated preview page displaying:

- File Name
- File Size
- Expiry Information
- Download Button

### Production Deployment
Application is deployed on AWS EC2 and served through Nginx.

---

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

### Frontend
- React
- Axios
- QRCode React

### DevOps & Deployment
- AWS EC2
- Ubuntu
- Nginx
- Git & GitHub

---

## Architecture

```text
User Browser
      │
      ▼
 React Frontend
      │
      ▼
 Spring Boot REST API
      │
      ▼
 Service Layer
      │
      ▼
 Spring Data JPA
      │
      ▼
 PostgreSQL Database

Hosted on AWS EC2
Nginx Reverse Proxy
```

---

## Application Flow

### Upload Flow

1. User selects a file
2. Frontend sends file to Spring Boot API
3. File metadata is stored in PostgreSQL
4. Unique short code is generated
5. Download link and QR code are returned

### Download Flow

1. User opens shared link
2. Preview page fetches file information
3. User clicks download
4. Backend validates:
   - Link exists
   - Link is not expired
   - One-time download rules
5. File is served to the user

---

## API Endpoints

### Upload File

```http
POST /api/files/upload
```

Uploads a file and returns a short download link.

---

### Get File Info

```http
GET /api/files/info/{shortCode}
```

Returns file metadata.

---

### Download File

```http
GET /api/files/download/{shortCode}
```

Downloads the file if the link is still valid.

---

## Engineering Challenges Solved

### One-Time Download Logic

Implemented file invalidation after successful download when one-time mode is enabled.

### Link Expiry Validation

Added configurable expiration durations and validation checks before file downloads.

### Production Deployment

Configured Spring Boot application on AWS EC2 behind Nginx reverse proxy.

### Mobile-Friendly Sharing

Integrated QR code generation for instant file sharing across devices.

---

## Screenshots

### Upload Page

<img width="607" height="498" alt="{AC1356E5-47CB-4862-848F-925DEB323FA7}" src="https://github.com/user-attachments/assets/2b330839-eb09-448f-b407-f23c91578677" />

### File Preview Page

<img width="612" height="538" alt="{051C116F-3437-431B-8E69-AF9166892C07}" src="https://github.com/user-attachments/assets/17fdf1fc-7503-4257-8e01-93e08b6ab564" />

### QR Code Sharing

<img width="401" height="404" alt="{515126FF-DEE7-4E7B-BCF2-5361427CA801}" src="https://github.com/user-attachments/assets/21ca0711-d3a0-44f3-b209-6be0ed139952" />


---

## Future Improvements

- Redis caching for faster lookups
- Docker containerization
- AWS S3 storage integration
- File type validation
- Upload size limits
- Rate limiting
- GitHub Actions CI/CD pipeline
- Spring Security authentication
- Admin dashboard

---

## Repository

Backend Source Code:

https://github.com/Shivaay21/quickdrop-backend

---

## Author

### Shivam Kumar

Backend Developer | Java | Spring Boot | PostgreSQL

GitHub:
https://github.com/Shivaay21

LinkedIn:
https://www.linkedin.com/in/shivam-kumar-74ab76227/

Live Application:
http://54.79.58.55

---

⭐ If you found this project interesting, consider giving it a star.
