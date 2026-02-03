# Nonito's Food - Sistema de Gestión de Prep Meals

Sistema web completo para gestión de prep meals que permite a administradores crear menús semanales y a clientes realizar pedidos con pago integrado.

## 📁 Estructura del Proyecto

```
Nonito's-Food/
├── PROJECT_CONVENTIONS.md         # ⭐ LEE ESTO PRIMERO - Convenciones del proyecto
├── NonitosRequisitos.md          # Documento completo de requisitos
│
├── backend/                       # Backend - Spring Boot
│   ├── tasks/                    # 📋 Tracking de progreso del backend
│   │   ├── TASKS.md             # Estado actual y tareas pendientes
│   │   └── TASK_X_COMPLETED.md  # Resúmenes de tareas completadas
│   └── nonitos-food-api/        # Código fuente
│       ├── src/
│       ├── pom.xml
│       └── README.md
│
└── frontend/                      # Frontend - React (próximamente)
    ├── tasks/                    # 📋 Tracking de progreso del frontend
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

### Frontend (próximamente)
```bash
cd frontend/nonitos-food-web
npm install
npm run dev
```

## 📊 Estado del Proyecto

- **Backend:** 1/10 tareas completadas (10%)
- **Frontend:** No iniciado

Ver detalles en:
- Backend: `backend/tasks/TASKS.md`
- Frontend: `frontend/tasks/TASKS.md`

## 📚 Documentación

- **Convenciones del proyecto:** `PROJECT_CONVENTIONS.md` ⭐
- **Requisitos completos:** `NonitosRequisitos.md`
- **Backend README:** `backend/nonitos-food-api/README.md`
- **Frontend README:** `frontend/nonitos-food-web/README.md` (próximamente)

## 🛠️ Stack Tecnológico

### Backend
- Spring Boot 3.2.2
- Java 21
- PostgreSQL (Supabase)
- Redis (Upstash)
- Flyway
- JWT
- Maven

### Frontend (próximamente)
- React 18
- Tailwind CSS
- shadcn/ui
- React Query
- React Router
- Vite

## 🔄 Continuar el Desarrollo

Si estás retomando el proyecto en una nueva sesión:

1. **Lee primero:** `PROJECT_CONVENTIONS.md`
2. **Revisa el progreso:** `backend/tasks/TASKS.md` o `frontend/tasks/TASKS.md`
3. **Carga la sesión (opcional):** `/load nonitos-backend-progress`

## 📝 Notas

- Todos los archivos de tracking de tareas están en `backend/tasks/` o `frontend/tasks/`
- Nunca crear archivos de tasks en la raíz del proyecto
- Cada módulo (backend/frontend) tiene su propio `TASKS.md` independiente

---

**Creado:** 2026-02-02  
**Última actualización:** 2026-02-02
