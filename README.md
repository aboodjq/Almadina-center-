Al-Madina Qurani Center System
This project aims to digitize the attendance and memorization tracking system at Al-Madina Qurani Center,
replacing the traditional paper-based system. The application is developed in Java using JavaFX for the frontend, connected to a MySQL database for backend data storage.

Features
Student Management: Add, update, and delete student information.
Memorization Tracking: Record and manage student progress in Quran memorization.
Attendance System: Track student attendance for each session.
Summaries: Generate and view reports summarizing attendance and progress.
User-Friendly Interface: Intuitive UI built with JavaFX.
Prerequisites
To run this system, you must set up the required environment and libraries. Follow the steps below to ensure proper functionality:

1. Install Java Development Kit (JDK)
Ensure you have JDK 11 or later installed. You can download it from the Oracle website or OpenJDK.

2. Install JavaFX SDK
Download the JavaFX SDK appropriate for your system from the Gluon website.

3. Install MySQL Server
Install MySQL Server on your machine.
Create a database named almadina_quran_center.
Use the SQL script provided in the /resources/sql/ folder to set up the database schema and initial data.
4. Configure Maven Dependencies
The project uses Maven for dependency management. Install Maven from Apache Maven's website if it's not already installed.

Add the following libraries in the pom.xml:

JavaFX (e.g., org.openjfx:javafx)
MySQL Connector (e.g., mysql:mysql-connector-java)
PDFBox (for report generation)
Linguaizer (for internationalization, if used)
5. Integrated Development Environment (IDE)
Use an IDE like IntelliJ IDEA or Eclipse with Maven support.

or further inquiries or support, contact Abdalrahman Alqannas at abood.alqannas@yahoo.com
