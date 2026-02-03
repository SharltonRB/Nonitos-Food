# Nonito's Food - Convenciones del Proyecto

## 📁 Estructura de Directorios

```
Nonito's-Food/
├── NonitosRequisitos.md          # Documento de requisitos completo
├── backend/
│   ├── tasks/                     # 📋 TODOS los archivos relacionados con tasks de BACKEND
│   │   ├── TASKS.md              # Tracker de progreso de backend
│   │   ├── TASK_1_COMPLETED.md   # Resumen de task completado
│   │   ├── TASK_2_COMPLETED.md   # etc...
│   │   └── ...
│   └── nonitos-food-api/         # Código fuente del backend
│       ├── src/
│       ├── pom.xml
│       └── README.md
└── frontend/
    ├── tasks/                     # 📋 TODOS los archivos relacionados con tasks de FRONTEND
    │   ├── TASKS.md              # Tracker de progreso de frontend
    │   ├── TASK_1_COMPLETED.md   # Resumen de task completado
    │   └── ...
    └── nonitos-food-web/         # Código fuente del frontend (cuando se cree)
        ├── src/
        ├── package.json
        └── README.md
```

---

## 🎯 Reglas de Organización

### 1. **Archivos de Tasks**
- ✅ **SIEMPRE** crear archivos de tasks dentro de `backend/tasks/` o `frontend/tasks/`
- ✅ **NUNCA** crear archivos de tasks en la raíz del proyecto
- ✅ Cada módulo (backend/frontend) tiene su propio `TASKS.md` independiente

### 2. **Nomenclatura de Archivos**
- `TASKS.md` - Tracker principal de progreso
- `TASK_X_COMPLETED.md` - Resumen de cada task completado (donde X es el número)
- `TASK_X_NOTES.md` - Notas adicionales si es necesario

### 3. **Contenido de TASKS.md**
Debe incluir:
- Estado general del proyecto (% completado)
- Lista de tareas completadas ✅
- Tarea actual 🔄
- Tareas pendientes ⏳
- Notas importantes 📝
- Instrucciones para continuar en nueva sesión 🚀

### 4. **Contenido de TASK_X_COMPLETED.md**
Debe incluir:
- Fecha de completado
- Lista de lo implementado
- Archivos clave creados/modificados
- Comandos para verificar
- Próximos pasos

---

## 🔄 Cómo Continuar el Trabajo en Nueva Sesión

### Opción 1: Cargar sesión guardada
```
/load nonitos-backend-progress
```

### Opción 2: Referencia manual
Al iniciar nueva sesión con Kiro, decir:

**Para Backend:**
```
"Lee el archivo backend/tasks/TASKS.md y continúa con la siguiente tarea pendiente"
```

**Para Frontend:**
```
"Lee el archivo frontend/tasks/TASKS.md y continúa con la siguiente tarea pendiente"
```

### Opción 3: Recordatorio de convenciones
```
"Lee PROJECT_CONVENTIONS.md y luego revisa backend/tasks/TASKS.md para continuar"
```

---

## 📝 Convenciones de Código

### Backend (Spring Boot)
- **Arquitectura:** Controller → Service → Repository
- **Paquetes:** config, controller, service, repository, model, dto, exception, security, util
- **DTOs:** Para requests/responses, nunca exponer entidades directamente
- **Excepciones:** Manejadas centralmente en GlobalExceptionHandler
- **Tests:** Cada clase de servicio debe tener su test correspondiente
- **Migraciones:** Flyway con nomenclatura `VX__Description.sql`

### Frontend (React - cuando se implemente)
- **Estructura:** components, pages, hooks, services, utils, types
- **Estilos:** Tailwind CSS + shadcn/ui
- **Estado:** React Query para servidor, Context API para global
- **Rutas:** React Router
- **Validación:** Zod + React Hook Form

---

## 🚀 Comandos Importantes

### Backend
```bash
# Ejecutar aplicación
cd backend/nonitos-food-api
mvn spring-boot:run

# Ejecutar tests
mvn test

# Build
mvn clean package

# Health check
curl http://localhost:8080/actuator/health
```

### Frontend (cuando se implemente)
```bash
# Ejecutar aplicación
cd frontend/nonitos-food-web
npm run dev

# Ejecutar tests
npm test

# Build
npm run build
```

---

## 📚 Documentos de Referencia

1. **NonitosRequisitos.md** - Documento completo de requisitos del sistema
2. **backend/tasks/TASKS.md** - Progreso del backend
3. **frontend/tasks/TASKS.md** - Progreso del frontend (cuando exista)
4. **backend/nonitos-food-api/README.md** - Documentación técnica del backend
5. **frontend/nonitos-food-web/README.md** - Documentación técnica del frontend (cuando exista)

---

## ⚠️ IMPORTANTE: Recordatorio para Kiro

**Al trabajar en este proyecto:**

1. ✅ Siempre lee este archivo (`PROJECT_CONVENTIONS.md`) al inicio
2. ✅ Coloca archivos de tasks en `backend/tasks/` o `frontend/tasks/`
3. ✅ Actualiza `TASKS.md` después de completar cada task
4. ✅ Crea `TASK_X_COMPLETED.md` al finalizar cada task
5. ✅ Sigue la arquitectura en capas definida
6. ✅ Escribe código mínimo y funcional (según implicit instruction)

---

**Creado:** 2026-02-02  
**Última actualización:** 2026-02-02
