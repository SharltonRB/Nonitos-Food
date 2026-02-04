# Nonito's Food - Sistema de Gestión de Prep Meals

Sistema web completo para gestión de prep meals que permite a administradores crear menús semanales y a clientes realizar pedidos con pago integrado.

## 📊 Estado del Proyecto

### Backend
- **Progreso:** 9/10 tareas completadas (90%)
- **Tests:** 72 tests unitarios (todos pasan ✅)
- **Última actualización:** 2026-02-03

### Frontend
- **Estado:** No iniciado

## ✅ Funcionalidades Implementadas

### Backend (Spring Boot)

#### 1. Sistema de Autenticación JWT ✅
- Registro de usuarios con roles (CLIENT, ADMIN, SUPER_ADMIN)
- Login con email y contraseña
- Refresh tokens con Redis (7 días)
- Verificación de email
- BCrypt para contraseñas (strength 12)

#### 2. Gestión de Perfiles de Usuario ✅
- Creación automática de perfil al registrar cliente
- Información personal y contacto de emergencia
- Gestión de alergias con niveles de severidad
- Restricciones dietéticas
- Preferencias de comidas y objetivos nutricionales
- Catálogos: 9 alergias, 8 restricciones dietéticas

#### 3. CRUD de Platillos ✅
- Gestión completa de platillos (Admin)
- Múltiples imágenes por platillo con orden
- Sistema de tags (10 pre-cargados)
- Alérgenos asociados
- Información nutricional completa
- Filtros avanzados con Specification API
- Paginación y ordenamiento

#### 4. Catálogos Públicos ✅
- Alergias, restricciones dietéticas, tags de platillos
- Endpoints públicos para frontend

#### 5. Gestión de Menús Semanales ✅
- Creación de menús semanales (Admin)
- Asignación de platillos por día y comida
- Validación: solo lunes como inicio de semana
- Solo un menú publicado por semana
- Cálculo automático de resumen nutricional
- Estados: DRAFT, PUBLISHED, ARCHIVED

#### 6. Sistema de Pedidos ✅
- Creación de pedidos por clientes
- Generación de código único y QR
- Máquina de estados de pedido
- Historial de cambios de estado
- Política de cancelación (24 horas)
- Cálculo automático de monto total

#### 7. Integración de Pagos ✅
- Mock de Stripe para tarjetas de crédito
- Soporte para transferencia bancaria
- Soporte para SINPE Móvil
- Verificación admin para pagos manuales
- Actualización automática de estado de pedido
- Tracking completo de transacciones

#### 8. Sistema de Notificaciones ✅
- Notificaciones basadas en templates
- 10 tipos de notificaciones pre-configuradas
- Mock de envío de emails (logs a consola)
- Tracking de estado leído/no leído
- Links a entidades relacionadas (pedidos, menús)
- Integración automática con eventos del sistema

#### 9. Panel de Administración ✅
- Dashboard con métricas en tiempo real
- Gestión de usuarios (listar, actualizar, eliminar)
- Métricas: usuarios, pedidos, ingresos, platillos, menús
- Endpoints protegidos con roles ADMIN/SUPER_ADMIN

## 📁 Estructura del Proyecto

```
Nonito's-Food/
├── README.md                      # Este archivo
├── PROJECT_CONVENTIONS.md         # ⭐ Convenciones del proyecto
├── NonitosRequisitos.md          # Documento completo de requisitos
│
├── docs/                          # Documentación adicional
│
├── backend/                       # Backend - Spring Boot
│   ├── tasks/                    # 📋 Tracking de progreso
│   │   ├── TASKS.md             # Estado actual y tareas pendientes
│   │   ├── TASK_1_COMPLETED.md  # Configuración inicial
│   │   ├── TASK_2_COMPLETED.md  # Autenticación JWT
│   │   ├── TASK_3_COMPLETED.md  # Perfiles de usuario
│   │   ├── TASK_4_COMPLETED.md  # CRUD de platillos
│   │   ├── TASK_5_COMPLETED.md  # Menús semanales
│   │   ├── TASK_6_COMPLETED.md  # Sistema de pedidos
│   │   ├── TASK_7_COMPLETED.md  # Integración de pagos
│   │   ├── TASK_8_COMPLETED.md  # Sistema de notificaciones
│   │   └── TASK_9_COMPLETED.md  # Panel de administración
│   └── nonitos-food-api/        # Código fuente
│       ├── src/
│       ├── pom.xml
│       └── README.md            # Documentación del backend
│
└── frontend/                      # Frontend - React (próximamente)
    ├── tasks/                    # 📋 Tracking de progreso
    │   └── TASKS.md             # Estado actual y tareas pendientes
    └── nonitos-food-web/        # Código fuente (cuando se cree)
```

## 🚀 Quick Start

### Backend
```bash
cd backend/nonitos-food-api
cp .env.example .env
# Editar .env con tus credenciales
mvn spring-boot:run
```

**Endpoints disponibles:**
- Health: `http://localhost:8080/actuator/health`
- API: `http://localhost:8080/api`

### Frontend (próximamente)
```bash
cd frontend/nonitos-food-web
npm install
npm run dev
```

## 🛠️ Stack Tecnológico

### Backend
- **Framework:** Spring Boot 3.2.2
- **Lenguaje:** Java 21
- **Base de datos:** PostgreSQL 15+ (Supabase)
- **Cache:** Redis 7+ (Upstash)
- **Migraciones:** Flyway
- **Seguridad:** Spring Security + JWT
- **Build:** Maven 3.8+
- **Testing:** JUnit 5 + Mockito (57 tests ✅)

### Frontend (próximamente)
- React 18
- Tailwind CSS
- shadcn/ui
- React Query
- React Router
- Vite

## 📚 Documentación

### Documentación Principal
- **Convenciones del proyecto:** `PROJECT_CONVENTIONS.md` ⭐
- **Requisitos completos:** `NonitosRequisitos.md`
- **Backend README:** `backend/nonitos-food-api/README.md`

### Tracking de Tareas
- **Backend:** `backend/tasks/TASKS.md`
- **Frontend:** `frontend/tasks/TASKS.md`

### Tareas Completadas
- `backend/tasks/TASK_1_COMPLETED.md` - Configuración inicial
- `backend/tasks/TASK_2_COMPLETED.md` - Autenticación JWT
- `backend/tasks/TASK_3_COMPLETED.md` - Perfiles de usuario
- `backend/tasks/TASK_4_COMPLETED.md` - CRUD de platillos
- `backend/tasks/TASK_5_COMPLETED.md` - Menús semanales
- `backend/tasks/TASK_6_COMPLETED.md` - Sistema de pedidos
- `backend/tasks/TASK_7_COMPLETED.md` - Integración de pagos
- `backend/tasks/TASK_8_COMPLETED.md` - Sistema de notificaciones
- `backend/tasks/TASK_9_COMPLETED.md` - Panel de administración

## 🔄 Continuar el Desarrollo

Si estás retomando el proyecto en una nueva sesión:

1. **Lee primero:** `PROJECT_CONVENTIONS.md`
2. **Revisa el progreso:** `backend/tasks/TASKS.md` o `frontend/tasks/TASKS.md`
3. **Revisa la última tarea completada:** `backend/tasks/TASK_X_COMPLETED.md`

## 🧪 Testing

### Backend
```bash
cd backend/nonitos-food-api
mvn test
```

**Cobertura actual:**
- AuthService: 7 tests
- JwtService: 8 tests
- ClientProfileService: 9 tests
- DishService: 9 tests
- WeeklyMenuService: 10 tests
- OrderService: 7 tests
- PaymentService: 7 tests
- NotificationService: 7 tests
- DashboardService: 8 tests
- **Total: 72 tests ✅**

## 🔐 Seguridad

- JWT con access token (30 min) y refresh token (7 días)
- Contraseñas hasheadas con BCrypt (strength 12)
- Roles: CLIENT, ADMIN, SUPER_ADMIN
- Endpoints públicos: Auth, catálogos, consulta de platillos
- Endpoints protegidos: Gestión de perfil, CRUD de platillos (Admin)

## 📝 Próximas Funcionalidades

### Backend
- [x] Gestión de menús semanales (Task 5) ✅
- [x] Sistema de pedidos (Task 6) ✅
- [x] Integración de pagos (Task 7) ✅
- [x] Sistema de notificaciones (Task 8) ✅
- [x] Panel de administración (Task 9) ✅
- [ ] Testing E2E y documentación (Task 10)

### Frontend
- [ ] Configuración inicial
- [ ] Autenticación y registro
- [ ] Perfil de usuario
- [ ] Catálogo de platillos
- [ ] Menús semanales
- [ ] Sistema de pedidos
- [ ] Pagos
- [ ] Panel de administración

## 🌐 Endpoints API Principales

### Autenticación
- `POST /api/auth/register` - Registro
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh` - Renovar token
- `POST /api/auth/verify-email` - Verificar email

### Perfiles
- `GET /api/profile` - Obtener perfil
- `PUT /api/profile` - Actualizar perfil
- `POST /api/profile/allergies` - Agregar alergia
- `POST /api/profile/restrictions` - Agregar restricción
- `PUT /api/profile/preferences` - Actualizar preferencias

### Platillos
- `POST /api/dishes` - Crear platillo (Admin)
- `GET /api/dishes` - Listar platillos con filtros
- `GET /api/dishes/{id}` - Obtener platillo
- `PUT /api/dishes/{id}` - Actualizar platillo (Admin)
- `DELETE /api/dishes/{id}` - Eliminar platillo (Admin)

### Catálogos
- `GET /api/catalogs/allergies` - Alergias
- `GET /api/catalogs/restrictions` - Restricciones dietéticas
- `GET /api/catalogs/tags` - Tags de platillos

### Menús Semanales
- `POST /api/menus` - Crear menú (Admin)
- `GET /api/menus/{id}` - Obtener menú
- `GET /api/menus/published` - Listar menús publicados
- `PUT /api/menus/{id}` - Actualizar menú (Admin)
- `POST /api/menus/{id}/publish` - Publicar menú (Admin)
- `DELETE /api/menus/{id}` - Eliminar menú (Admin)

### Pedidos
- `POST /api/orders` - Crear pedido (Client)
- `GET /api/orders/{id}` - Obtener pedido
- `GET /api/orders/my-orders` - Mis pedidos (Client)
- `GET /api/orders` - Todos los pedidos (Admin)
- `PUT /api/orders/{id}/status` - Actualizar estado (Admin)
- `POST /api/orders/{id}/cancel` - Cancelar pedido (Client)

### Pagos
- `POST /api/payments/credit-card` - Pago con tarjeta (Client)
- `POST /api/payments/manual` - Pago manual (Client)
- `POST /api/payments/{id}/verify` - Verificar pago (Admin)
- `GET /api/payments/order/{orderId}` - Transacciones de pedido
- `GET /api/payments/{id}` - Obtener transacción

### Notificaciones
- `GET /api/notifications` - Obtener notificaciones del usuario
- `GET /api/notifications/unread` - Obtener notificaciones no leídas
- `GET /api/notifications/unread/count` - Contador de no leídas
- `PUT /api/notifications/{id}/read` - Marcar como leída
- `PUT /api/notifications/read-all` - Marcar todas como leídas

### Panel de Administración
- `GET /api/admin/dashboard/metrics` - Métricas del dashboard (Admin)
- `GET /api/admin/users` - Listar usuarios con paginación (Admin)
- `GET /api/admin/users/{id}` - Obtener usuario por ID (Admin)
- `PUT /api/admin/users/{id}` - Actualizar usuario (Admin)
- `DELETE /api/admin/users/{id}` - Eliminar usuario (Admin)

## 🐛 Troubleshooting
- `PUT /api/notifications/{id}/read` - Marcar como leída
- `PUT /api/notifications/read-all` - Marcar todas como leídas

## 🐛 Troubleshooting

### Backend no inicia
- Verifica que PostgreSQL y Redis estén accesibles
- Revisa las credenciales en `.env`
- Verifica que el puerto 8080 esté disponible

### Tests fallan
- Asegúrate de tener Java 21 instalado
- Ejecuta `mvn clean install` primero

## 👥 Contribución

Ver `PROJECT_CONVENTIONS.md` para guías de estilo y convenciones del proyecto.

## 📄 Licencia

Privado - Nonito's Food © 2026

---

**Creado:** 2026-02-02  
**Última actualización:** 2026-02-03
