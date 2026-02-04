# Nonito's Food API

Backend para el sistema de gestión de prep meals de Nonito's Food.

## 📊 Estado del Proyecto

- **Progreso:** 4/10 tareas completadas (40%)
- **Tests:** 33 tests unitarios (todos pasan ✅)
- **Última actualización:** 2026-02-03

## ✅ Funcionalidades Implementadas

### 1. Sistema de Autenticación JWT
- Registro de usuarios (CLIENT, ADMIN, SUPER_ADMIN)
- Login con email y contraseña
- Refresh tokens (7 días de validez)
- Verificación de email
- Tokens almacenados en Redis

**Endpoints:**
- `POST /api/auth/register` - Registro
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh` - Renovar token
- `POST /api/auth/verify-email?token=xxx` - Verificar email

### 2. Gestión de Perfiles de Usuario
- Perfil automático al registrar cliente
- Información personal y contacto de emergencia
- Gestión de alergias con severidad
- Restricciones dietéticas
- Preferencias de comidas y objetivos nutricionales

**Endpoints:**
- `GET /api/profile` - Obtener perfil
- `PUT /api/profile` - Actualizar perfil
- `POST /api/profile/allergies` - Agregar alergia
- `DELETE /api/profile/allergies/{id}` - Eliminar alergia
- `POST /api/profile/restrictions` - Agregar restricción
- `DELETE /api/profile/restrictions/{id}` - Eliminar restricción
- `PUT /api/profile/preferences` - Actualizar preferencias

### 3. CRUD de Platillos
- Gestión completa de platillos (Admin)
- Múltiples imágenes por platillo
- Sistema de tags (High Protein, Vegan, etc.)
- Alérgenos asociados
- Información nutricional completa
- Filtros avanzados con paginación

**Endpoints:**
- `POST /api/dishes` - Crear platillo (Admin)
- `GET /api/dishes/{id}` - Obtener platillo
- `GET /api/dishes` - Listar con filtros
- `PUT /api/dishes/{id}` - Actualizar platillo (Admin)
- `DELETE /api/dishes/{id}` - Eliminar platillo (Admin)

**Filtros disponibles:**
- category: BREAKFAST, LUNCH, DINNER
- isActive: true/false
- minPrice/maxPrice: rango de precios
- tagName: filtrar por tag
- Paginación: page, size, sortBy, sortDir

### 4. Catálogos Públicos
- Alergias (9 pre-cargadas)
- Restricciones dietéticas (8 pre-cargadas)
- Tags de platillos (10 pre-cargados)

**Endpoints:**
- `GET /api/catalogs/allergies` - Catálogo de alergias
- `GET /api/catalogs/restrictions` - Catálogo de restricciones
- `GET /api/catalogs/tags` - Catálogo de tags

## 🚀 Requisitos

- Java 21
- Maven 3.8+
- PostgreSQL 15+ (Supabase)
- Redis 7+ (Upstash)

## ⚙️ Configuración Local

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd backend/nonitos-food-api
```

### 2. Configurar variables de entorno
```bash
cp .env.example .env
```

Editar `.env` con tus credenciales:
```env
DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
DATABASE_USERNAME=your-username
DATABASE_PASSWORD=your-password
REDIS_HOST=your-redis-host
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password
JWT_SECRET=your-super-secret-jwt-key-min-256-bits
JWT_EXPIRATION=1800000
JWT_REFRESH_EXPIRATION=604800000
```

### 3. Instalar dependencias
```bash
mvn clean install
```

### 4. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### 5. Verificar health check
```bash
curl http://localhost:8080/actuator/health
```

## 🗄️ Base de Datos

Las migraciones de Flyway se ejecutan automáticamente al iniciar la aplicación.

**Migraciones actuales:**
- V1: Tabla de usuarios
- V2: Perfiles de cliente, alergias, restricciones, preferencias
- V3: Platillos, imágenes, tags, alérgenos

## 🧪 Testing

### Ejecutar todos los tests
```bash
mvn test
```

### Ejecutar tests específicos
```bash
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=ClientProfileServiceTest
mvn test -Dtest=DishServiceTest
```

### Cobertura actual
- AuthService: 7 tests
- JwtService: 8 tests
- ClientProfileService: 9 tests
- DishService: 9 tests
- **Total: 33 tests ✅**

## 📦 Build para Producción

```bash
mvn clean package -DskipTests
```

El JAR se generará en `target/nonitos-food-api-0.0.1-SNAPSHOT.jar`

## 📁 Estructura del Proyecto

```
src/main/java/com/nonitos/food/
├── config/              # Configuraciones (Security, CORS, JPA)
├── controller/          # Controladores REST
│   ├── AuthController
│   ├── ClientProfileController
│   ├── DishController
│   └── CatalogController
├── service/             # Lógica de negocio
│   ├── AuthService
│   ├── JwtService
│   ├── ClientProfileService
│   └── DishService
├── repository/          # Repositorios JPA
├── model/               # Entidades JPA
│   ├── User
│   ├── ClientProfile
│   ├── Allergy
│   ├── DietaryRestriction
│   ├── ClientPreferences
│   ├── Dish
│   ├── DishImage
│   ├── DishTag
│   └── ...
├── dto/                 # Data Transfer Objects
│   ├── auth/
│   ├── profile/
│   └── dish/
├── exception/           # Excepciones personalizadas
├── security/            # JWT Filter
└── util/                # Utilidades

src/main/resources/
├── application.yml      # Configuración principal
├── application-dev.yml  # Configuración desarrollo
├── application-prod.yml # Configuración producción
└── db/migration/        # Migraciones Flyway
```

## 🔐 Seguridad

- **Autenticación:** JWT con access token (30 min) y refresh token (7 días)
- **Contraseñas:** BCrypt con strength 12
- **Roles:** CLIENT, ADMIN, SUPER_ADMIN
- **Endpoints públicos:** Auth, catálogos, consulta de platillos
- **Endpoints protegidos:** Gestión de perfil, CRUD de platillos (Admin)

## 🛠️ Tecnologías

- **Framework:** Spring Boot 3.2.2
- **Lenguaje:** Java 21
- **Base de datos:** PostgreSQL 15+
- **Cache:** Redis 7+
- **Migraciones:** Flyway
- **Seguridad:** Spring Security + JWT (jjwt 0.12.3)
- **Validación:** Jakarta Bean Validation
- **Documentación:** Javadoc
- **Build:** Maven 3.8+
- **Testing:** JUnit 5 + Mockito

## 📚 Documentación Adicional

- **Requisitos completos:** `/docs/NonitosRequisitos.md`
- **Convenciones:** `/PROJECT_CONVENTIONS.md`
- **Tracking de tareas:** `/backend/tasks/TASKS.md`
- **Tareas completadas:** `/backend/tasks/TASK_X_COMPLETED.md`

## 🐛 Troubleshooting

### Error de conexión a PostgreSQL
Verifica que las credenciales en `.env` sean correctas y que la base de datos esté accesible.

### Error de conexión a Redis
Verifica que Redis esté corriendo y las credenciales sean correctas.

### Tests fallan
Asegúrate de tener H2 en el classpath (incluido en `pom.xml`).

## 📝 Próximas Funcionalidades

- [ ] Gestión de menús semanales
- [ ] Sistema de pedidos
- [ ] Integración de pagos
- [ ] Sistema de notificaciones
- [ ] Panel de administración
- [ ] Testing E2E

## 👥 Contribución

Ver `PROJECT_CONVENTIONS.md` para guías de estilo y convenciones del proyecto.

## 📄 Licencia

Privado - Nonito's Food © 2026
