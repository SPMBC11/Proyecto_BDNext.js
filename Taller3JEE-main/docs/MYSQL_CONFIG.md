# Configuración para MySQL (Producción)

## Instalación y Configuración MySQL

### 1. Instalar MySQL Server
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install mysql-server

# macOS
brew install mysql
brew services start mysql

# Windows
# Descargar desde https://dev.mysql.com/downloads/mysql/
```

### 2. Crear Base de Datos y Usuario
```bash
mysql -u root -p <<EOF
SOURCE docs/schema.sql;

-- Crear usuario para la aplicación
CREATE USER 'taller3jee'@'localhost' IDENTIFIED BY 'SecurePassword123!';
GRANT ALL PRIVILEGES ON taller3jee.* TO 'taller3jee'@'localhost';
FLUSH PRIVILEGES;

-- Verificar
SHOW GRANTS FOR 'taller3jee'@'localhost';
EOF
```

### 3. Configurar application-prod.properties
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/taller3jee?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=taller3jee
spring.datasource.password=SecurePassword123!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.show-sql=false

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### 4. Ejecutar con Producción
```bash
mvn clean package -Pprod -DskipTests
java -jar target/taller3jee-ed-1.0.0.jar --spring.profiles.active=prod
```

### 5. Monitoreo
```bash
# Ver status de MySQL
sudo systemctl status mysql

# Conectar a la BD
mysql -u taller3jee -p -D taller3jee

# Ver logs de MySQL
sudo tail -f /var/log/mysql/error.log
```

## Backup y Restauración

### Backup Completo
```bash
mysqldump -u taller3jee -p taller3jee > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restaurar desde Backup
```bash
mysql -u taller3jee -p taller3jee < backup_20260423_101500.sql
```

### Backup Incremental
```bash
# Habilitar binary logs en my.cnf
log_bin = /var/log/mysql/mysql-bin.log
server-id = 1

# Crear punto de recuperación
mysqldump -u taller3jee -p --single-transaction --master-data taller3jee > backup_full.sql

# Ver binary logs
mysqlbinlog /var/log/mysql/mysql-bin.000001
```

## Performance Tuning

### Optimización de my.cnf
```ini
[mysqld]
# Memory
max_connections = 200
max_allowed_packet = 256M
sort_buffer_size = 4M
bulk_insert_buffer_size = 16M
tmp_table_size = 32M
max_heap_table_size = 32M

# InnoDB
innodb_buffer_pool_size = 1G
innodb_log_file_size = 512M
innodb_file_per_table = 1
innodb_flush_log_at_trx_commit = 2

# Logging
log_queries_not_using_indexes = 1
long_query_time = 2
slow_query_log = 1
slow_query_log_file = /var/log/mysql/slow-query.log
```

### Ver Estadísticas
```sql
-- Ver conexiones activas
SHOW PROCESSLIST;

-- Ver tamaño de bases de datos
SELECT 
  table_schema,
  ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) as size_mb
FROM information_schema.tables
GROUP BY table_schema;

-- Ver índices sin usar
SELECT * FROM sys.schema_unused_indexes;

-- Ver queries lentas
SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;
```

### Mantenimiento
```sql
-- Analizar tablas
ANALYZE TABLE tema, estudiante, progreso_estudiante, recomendacion;

-- Optimizar tablas
OPTIMIZE TABLE tema, estudiante, progreso_estudiante, recomendacion;

-- Reparar tablas (si es necesario)
REPAIR TABLE tema, estudiante, progreso_estudiante, recomendacion;
```

## Replicación (Opcional)

### Setup Master-Slave
```sql
-- En Master
CHANGE MASTER TO MASTER_HOST='slave-host', MASTER_USER='slave_user', MASTER_PASSWORD='password';
START SLAVE;

-- En Slave
SHOW SLAVE STATUS\G
```

## Seguridad

### Cambiar Contraseña del Root
```bash
mysqladmin -u root password "NewPassword123!"
```

### Crear Usuarios por Rol
```sql
-- Usuario Read-Only
CREATE USER 'taller3jee_read'@'localhost' IDENTIFIED BY 'ReadPassword123!';
GRANT SELECT ON taller3jee.* TO 'taller3jee_read'@'localhost';

-- Usuario para Backups
CREATE USER 'taller3jee_backup'@'localhost' IDENTIFIED BY 'BackupPassword123!';
GRANT SELECT, LOCK TABLES, SHOW VIEW, RELOAD, REPLICATION CLIENT ON taller3jee.* TO 'taller3jee_backup'@'localhost';

-- Usuario para Monitoreo
CREATE USER 'taller3jee_monitor'@'localhost' IDENTIFIED BY 'MonitorPassword123!';
GRANT USAGE ON *.* TO 'taller3jee_monitor'@'localhost';
GRANT SELECT ON mysql.* TO 'taller3jee_monitor'@'localhost';
```

### Encriptación SSL
```bash
# Generar certificados
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/mysql/server-key.pem -out /etc/mysql/server-cert.pem
chmod 644 /etc/mysql/server-*.pem

# Configurar en my.cnf
[mysqld]
ssl-ca = /etc/mysql/server-ca.pem
ssl-cert = /etc/mysql/server-cert.pem
ssl-key = /etc/mysql/server-key.pem

# Requerir SSL
GRANT ALL PRIVILEGES ON taller3jee.* TO 'taller3jee'@'localhost' REQUIRE SSL;
```

