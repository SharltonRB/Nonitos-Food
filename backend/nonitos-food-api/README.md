# Nonito's Food API

Backend para el sistema de gestión de prep meals de Nonito's Food.

## Requisitos

- Java 21
- Maven 3.8+
- PostgreSQL 15+
- Redis 7+

## Configuración Local

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd backend/nonitos-food-api
```

2. **Configurar variables de entorno**
```bash
cp .env.example .env
# Editar .env con tus credenciales
```

3. **Instalar dependencias**
```bash
mvn clean install
```

4. **Ejecutar migraciones de base de datos**
Las migraciones se ejecutan automáticamente al iniciar la aplicación con Flyway.

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints Principales

- Health Check: `GET /actuator/health`
- API Base: `/api`

## Estructura del Proyecto

```
src/main/java/com/nonitos/food/
├── config/          # Configuraciones de Spring
├── controller/      # Controladores REST
├── service/         # Lógica de negocio
├── repository/      # Repositorios JPA
├── model/           # Entidades JPA
├── dto/             # Data Transfer Objects
├── exception/       # Excepciones personalizadas
├── security/        # Configuración de seguridad
└── util/            # Utilidades
```

## Testing

```bash
mvn test
```

## Build para Producción

```bash
mvn clean package -DskipTests
```

## Tecnologías

- Spring Boot 3.2.2
- Java 21
- PostgreSQL
- Redis
- Flyway
- JWT
- Lombok
