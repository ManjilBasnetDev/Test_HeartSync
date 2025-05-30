# HeartSync Database Setup

This document provides instructions for setting up the HeartSync database.

## Prerequisites

1. MySQL Server 8.0 or later installed
2. MySQL Connector/J (JDBC driver) added to your project
3. Access to MySQL with root privileges

## Database Setup Instructions

1. Install MySQL if you haven't already:
   - Windows: Download and install from MySQL website
   - Linux: `sudo apt-get install mysql-server`
   - Mac: `brew install mysql`

2. Start MySQL Server:
   - Windows: MySQL service should start automatically
   - Linux: `sudo systemctl start mysql`
   - Mac: `mysql.server start`

3. Log into MySQL as root:
   ```bash
   mysql -u root -p
   ```

4. Run the setup script:
   ```bash
   source path/to/setup.sql
   ```

5. Create a new MySQL user for the application (optional but recommended):
   ```sql
   CREATE USER 'heartsync_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON heartsync.* TO 'heartsync_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

6. Update the DatabaseConfig.java file with your database credentials:
   - Update the USER and PASSWORD fields with your MySQL credentials
   - If you created a new user, use those credentials instead of root

## Database Schema

The database consists of three main tables:

1. `users` - Stores user authentication information
   - id (Primary Key)
   - username
   - password (hashed)
   - email
   - created_at
   - updated_at

2. `user_profiles` - Stores user profile information
   - user_id (Foreign Key to users.id)
   - first_name
   - last_name
   - date_of_birth
   - gender
   - profile_picture
   - bio

3. `health_data` - Stores user health measurements
   - id (Primary Key)
   - user_id (Foreign Key to users.id)
   - heart_rate
   - blood_pressure_systolic
   - blood_pressure_diastolic
   - measurement_date
   - notes

## Adding MySQL Connector to Your Project

1. Download MySQL Connector/J from the official MySQL website
2. Add the connector JAR to your project's libraries
3. If using NetBeans:
   - Right-click on Libraries
   - Select "Add JAR/Folder"
   - Navigate to the MySQL Connector JAR file
   - Click Open 