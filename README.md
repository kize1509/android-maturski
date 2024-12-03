# SCHOOL TIMETABLE CLIENT APP (Android)

This repository contains the Android client application for managing high school timetables. It is developed in Java and serves as the client-side part of the project, with the backend implemented as a separate project using Express.js.

## Key Features
- **Real-Time Chat**: A bound service is used to implement a real-time chat feature, enabling communication between users.
- **Timetable Management**: View and manage timetables as images, providing a user-friendly interface for students and teachers.
- **CRUD Operations**: Full Create, Read, Update, and Delete functionality for all entities, accessible through an admin interface.
- **Backend Communication**: The app communicates with the backend via API endpoints exposed by an Express.js server.

## Project Structure

. ├── .idea # Android Studio project files 
  ├── app # Main application source code 
  ├── gradle/wrapper # Gradle wrapper files 
  ├── .gitignore # Git ignored files 
  ├── README.md # Project documentation 
  ├── build.gradle # Build configuration 
  ├── gradle.properties # Gradle properties configuration 
  ├── gradlew # Gradle wrapper executable 
  ├── gradlew.bat # Gradle wrapper executable for Windows 
  └── settings.gradle # Project settings


## Getting Started
### Prerequisites
- **Android Studio**: Install the latest version of Android Studio.
- **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed.

### Setup Instructions
1. Clone this repository:
   ```bash
   git clone https://github.com/kize1509/school-timetable-client-android.git
   cd school-timetable-client-android

    Open the project in Android Studio.
    Sync the Gradle files to download dependencies.
    Run the application on an emulator or physical device.

Backend Setup

The client app requires the backend server to be running. The backend is developed using Express.js and exposes APIs for managing timetable data and handling real-time chat.
Usage

    Teachers: View timetables and participate in real-time chat.
    Students: View timetables for own class.
    Admin: Manage timetable data and perform CRUD operations on all entities through the admin interface.

Contributing

Contributions are welcome! Please follow these steps:

    Fork the repository.
    Create a new feature branch:

git checkout -b feature/your-feature-name

Commit your changes:

git commit -m "Add your feature description"

Push to the branch:

    git push origin feature/your-feature-name

    Open a pull request.

License

This project is licensed under the MIT License.
