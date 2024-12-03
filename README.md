# 🥷techtasktain🥷

# Project decription:
- 🫡Welcome to the techtasktain!🫡
  
## Setup

To set up the project, follow these steps:

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) 17 or higher
- Apache Maven
- Apache Tomcat vesion 9 or higher
- Utilise Spring Boot 2.7 or higher
- DataBase: MYSQL

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Replace Placeholders:
To connect to your DB, you should replace PlaceHolders in .env and application.properties
- Open package resources and open application.properties in your project.
- Locate the placeholders that need to be replaced.
- These placeholders might include values such as
- spring.datasource.url=URL - replace with your url to db
- spring.datasource.username=NAME - replace with your name to db
- spring.datasource.password=PASSWORD - replace with your password to db


# Features 🤌:

## User 🤵‍♂️
- Registration like a user/admin
- Authentication like a user/admin

## Role 🙎‍♂️
- Create/update/remove a role
- Get role by roleName

## Video 🎥
- Publish Video
- Update Video
- Load Video
- Play Video
- Get List of Videos
- Delete Video
- Search Video by Params


## Controllers 🕹

## AuthController 🤵‍♂️
- Login to app -> api/auth/login - Post
- Register in app -> api/auth/register - Post

## Video 🎥
- Get all videos -> api/videos - Get
- Load video by id -> api/videos/{id} - Get
- Publish video -> api/videos - Post
- Soft delete video by id -> api/videos/{id} - Delete
- Update video info by id -> api/videos/{id} - Put
- Search videos by param -> api/videos/search?titles= or api/videos/search?directors= or api/videos/search?titles=&directors= - Get
- Play video by id -> api/videos/{id}/play - Get
