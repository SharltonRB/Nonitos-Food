# Nonito's Food API

Backend para el sistema de gestión de prep meals de Nonito's Food.

## 📊 Estado del Proyecto

- **Progreso:** 8/10 tareas completadas (80%)
- **Tests:** 64 tests unitarios (todos pasan ✅)
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

### 5. Gestión de Menús Semanales
- Creación de menús semanales (Admin)
- Asignación de platillos por día y comida
- Validación: solo lunes como inicio de semana
- Solo un menú publicado por semana
- Cálculo automático de resumen nutricional
- Estados: DRAFT, PUBLISHED, ARCHIVED

**Endpoints:**
- `POST /api/menus` - Crear menú (Admin)
- `GET /api/menus/{id}` - Obtener menú
- `GET /api/menus/published` - Listar menús publicados
- `PUT /api/menus/{id}` - Actualizar menú (Admin)
- `POST /api/menus/{id}/publish` - Publicar menú (Admin)
- `DELETE /api/menus/{id}` - Eliminar menú (Admin)

### 6. Sistema de Pedidos
- Creación de pedidos por clientes
- Generación de código único (8 caracteres)
- Mock de QR code para pickup
- Máquina de estados de pedido
- Historial de cambios de estado
- Política de cancelación (24 horas antes)
- Cálculo automático de monto total

**Endpoints:**
- `POST /api/orders` - Crear pedido (Client)
- `GET /api/orders/{id}` - Obtener pedido
- `GET /api/orders/my-orders` - Mis pedidos (Client)
- `GET /api/orders` - Todos los pedidos (Admin)
- `PUT /api/orders/{id}/status` - Actualizar estado (Admin)
- `POST /api/orders/{id}/cancel` - Cancelar pedido (Client)

### 7. Integración de Pagos
- Mock de Stripe para tarjetas de crédito
- Soporte para transferencia bancaria y SINPE Móvil
- Verificación admin para pagos manuales
- Actualización automática de estado de pedido
- Tracking completo de transacciones

**Endpoints:**
- `POST /api/payments/credit-card` - Pago con tarjeta (Client)
- `POST /api/payments/manual` - Pago manual (Client)
- `POST /api/payments/{id}/verify` - Verificar pago (Admin)
- `GET /api/payments/order/{orderId}` - Transacciones de pedido
- `GET /api/payments/{id}` - Obtener transacción

### 8. Sistema de Notificaciones
- Notificaciones basadas en templates
- 10 tipos de notificaciones pre-configuradas
- Mock de envío de emails (logs a consola)
- Tracking de estado leído/no leído
- Links a entidades relacionadas (pedidos, menús)

**Endpoints:**
- `GET /api/notifications` - Obtener notificaciones del usuario
- `GET /api/notifications/unread` - Obtener notificaciones no leídas
- `GET /api/notifications/unread/count` - Contador de no leídas
- `PUT /api/notifications/{id}/read` - Marcar como leída
- `PUT /api/notifications/read-all` - Marcar todas como leídas

**Tipos de notificaciones:**
- ORDER_CREATED - Pedido creado
- ORDER_PAID - Pedido pagado
- ORDER_CONFIRMED - Pedido confirmado
- ORDER_READY - Pedido listo para recoger
- ORDER_COMPLETED - Pedido completado
- ORDER_CANCELLED - Pedido cancelado
- PAYMENT_RECEIVED - Pago recibido
- PAYMENT_VERIFIED - Pago verificado
- MENU_PUBLISHED - Nuevo menú publicado
- MENU_REMINDER - Recordatorio de menú

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
- V4: Menús semanales y días del menú
- V5: Pedidos e historial de estados
- V6: Transacciones de pago
- V7: Notificaciones y templates de notificaciones

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
mvn test -Dtest=WeeklyMenuServiceTest
mvn test -Dtest=OrderServiceTest
mvn test -Dtest=PaymentServiceTest
mvn test -Dtest=NotificationServiceTest
```

### Cobertura actual
- AuthService: 7 tests
- JwtService: 8 tests
- ClientProfileService: 9 tests
- DishService: 9 tests
- WeeklyMenuService: 10 tests
- OrderService: 7 tests
- PaymentService: 7 tests
- NotificationService: 7 tests
- **Total: 64 tests ✅**

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
│   ├── CatalogController
│   ├── WeeklyMenuController
│   ├── OrderController
│   ├── PaymentController
│   └── NotificationController
├── service/             # Lógica de negocio
│   ├── AuthService
│   ├── JwtService
│   ├── ClientProfileService
│   ├── DishService
│   ├── WeeklyMenuService
│   ├── OrderService
│   ├── PaymentService
│   └── NotificationService
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
│   ├── WeeklyMenu
│   ├── MenuDay
│   ├── Order
│   ├── OrderStatusHistory
│   ├── Transaction
│   ├── Notification
│   ├── NotificationTemplate
│   └── ...
├── dto/                 # Data Transfer Objects
│   ├── auth/
│   ├── profile/
│   ├── dish/
│   ├── menu/
│   ├── order/
│   ├── payment/
│   └── notification/
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

- [x] Gestión de menús semanales ✅
- [x] Sistema de pedidos ✅
- [x] Integración de pagos ✅
- [x] Sistema de notificaciones ✅
- [ ] Panel de administración (Task 9)
- [ ] Testing E2E y documentación (Task 10)
- [ ] Sistema de notificaciones
- [ ] Panel de administración
- [ ] Testing E2E

## 👥 Contribución

Ver `PROJECT_CONVENTIONS.md` para guías de estilo y convenciones del proyecto.

## 📄 Licencia

Privado - Nonito's Food © 2026
