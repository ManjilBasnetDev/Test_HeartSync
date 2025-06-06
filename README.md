# HeartSync Dating App

A modern dating application built with Java Swing and MySQL.

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/HeartSyncDatingApp.git
cd HeartSyncDatingApp
```

2. Create MySQL database:
```sql
CREATE DATABASE heartsync_db;
```

3. Update database configuration:
Edit `src/heartsyncdatingapp/database/DatabaseConnection.java` with your MySQL credentials.

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn exec:java -Dexec.mainClass="heartsyncdatingapp.view.Register"
```

## Features

- User registration with password validation
- Modern UI with rounded corners and smooth animations
- Password strength validation
- User/Admin role selection
- MySQL database integration

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.