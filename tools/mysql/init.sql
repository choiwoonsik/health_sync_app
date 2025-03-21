CREATE DATABASE IF NOT EXISTS health_sync DEFAULT CHARSET = utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;
CREATE DATABASE IF NOT EXISTS health_sync_test DEFAULT CHARSET = utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;
CREATE USER IF NOT EXISTS 'health_sync'@'%' IDENTIFIED BY 'health_sync';
GRANT ALL PRIVILEGES ON *.* TO 'health_sync'@'%';
FLUSH PRIVILEGES;