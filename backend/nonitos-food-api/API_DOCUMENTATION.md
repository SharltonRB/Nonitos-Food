# Nonito's Food API - Documentación

## 📚 Acceso a la Documentación

### Swagger UI (Interactivo)
Una vez que la aplicación esté corriendo, accede a:
```
http://localhost:8080/swagger-ui.html
```

Aquí podrás:
- Ver todos los endpoints disponibles
- Probar las APIs directamente desde el navegador
- Ver ejemplos de requests y responses
- Autenticarte con JWT para probar endpoints protegidos

### OpenAPI Specification
- **JSON:** `http://localhost:8080/v3/api-docs`
- **YAML:** `http://localhost:8080/v3/api-docs.yaml`

Usa estos archivos para:
- Generar clientes en otros lenguajes
- Importar a Postman/Insomnia
- Compartir con el equipo frontend

## 🔐 Autenticación en Swagger

1. Registra un usuario o usa credenciales existentes
2. Haz login en `/api/auth/login`
3. Copia el `accessToken` de la respuesta
4. Click en el botón "Authorize" (🔒) en la parte superior
5. Ingresa: `Bearer {tu-token-aqui}`
6. Click "Authorize"
7. Ahora puedes probar endpoints protegidos

## 📋 Grupos de Endpoints

### Authentication
- Registro de usuarios
- Login
- Refresh token
- Verificación de email

### Client Profile
- Gestión de perfil personal
- Alergias y restricciones dietéticas
- Preferencias nutricionales

### Dishes (Platillos)
- CRUD de platillos (Admin)
- Consulta pública con filtros
- Gestión de imágenes y tags

### Catalogs
- Alergias disponibles
- Restricciones dietéticas
- Tags de platillos

### Weekly Menus
- Creación y gestión de menús (Admin)
- Consulta de menús publicados
- Resumen nutricional

### Orders (Pedidos)
- Creación de pedidos
- Consulta de mis pedidos
- Gestión de estados (Admin)
- Cancelación con política de 24h

### Payments (Pagos)
- Pago con tarjeta (mock Stripe)
- Pago manual (transferencia/SINPE)
- Verificación de pagos (Admin)

### Notifications
- Consulta de notificaciones
- Marcar como leídas
- Contador de no leídas

### Admin Dashboard
- Métricas del sistema
- Gestión de usuarios
- Reportes

## 🧪 Testing

### Tests Unitarios
```bash
mvn test
```

**Cobertura:** 72 tests unitarios

### Tests de Integración
```bash
mvn test -Dtest=*IntegrationTest
```

**Cobertura:** 3 tests de integración

### Todos los Tests
```bash
mvn test
```

**Total:** 75 tests ✅

## 📊 Métricas de Calidad

- **Tests:** 75 (100% pasan)
- **Cobertura:** Servicios principales cubiertos
- **Documentación:** OpenAPI 3.0 completa
- **Endpoints:** 40+ endpoints documentados

## 🚀 Próximos Pasos

1. **Aumentar cobertura de tests de integración**
   - Tests para cada módulo principal
   - Tests de flujos completos (registro → pedido → pago)

2. **Agregar tests E2E**
   - Usar TestContainers para PostgreSQL y Redis
   - Simular flujos de usuario completos

3. **Mejorar documentación**
   - Agregar más ejemplos en Swagger
   - Documentar códigos de error
   - Guías de uso por rol (Cliente, Admin)

4. **Performance testing**
   - Tests de carga con JMeter
   - Optimización de queries N+1
   - Caching strategies

## 📖 Recursos Adicionales

- **Código fuente:** `/backend/nonitos-food-api/src`
- **Tests:** `/backend/nonitos-food-api/src/test`
- **Migraciones:** `/backend/nonitos-food-api/src/main/resources/db/migration`
- **Configuración:** `/backend/nonitos-food-api/src/main/resources/application.yml`
