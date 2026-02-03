# Guía de Pruebas con Swagger UI

## Acceso

1. Asegúrate de que la aplicación esté corriendo:
   ```bash
   cd backend/nonitos-food-api
   mvn spring-boot:run
   ```

2. Abre Swagger UI en tu navegador:
   - **URL**: http://localhost:8080/swagger-ui/index.html
   - **OpenAPI JSON**: http://localhost:8080/v3/api-docs
   - **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## Probar Endpoints sin Autenticación

### 1. Registrar un nuevo usuario

1. Busca el endpoint `POST /api/auth/register`
2. Haz clic en el endpoint para expandirlo
3. Haz clic en **"Try it out"**
4. Edita el JSON de ejemplo:
   ```json
   {
     "email": "test@example.com",
     "password": "SecurePass123",
     "fullName": "Test User",
     "phoneNumber": "+506 8888-8888"
   }
   ```
5. Haz clic en **"Execute"**
6. Verás la respuesta con:
   - **Código de estado**: 201 (Created)
   - **Response body**: Incluye `accessToken` y `refreshToken`
   - **Curl command**: El comando equivalente

### 2. Login

1. Busca el endpoint `POST /api/auth/login`
2. Haz clic en **"Try it out"**
3. Usa las credenciales del usuario que registraste:
   ```json
   {
     "email": "test@example.com",
     "password": "SecurePass123"
   }
   ```
4. Haz clic en **"Execute"**
5. **IMPORTANTE**: Copia el `accessToken` de la respuesta

## Configurar Autenticación JWT

Para probar endpoints protegidos, necesitas configurar el token JWT:

1. Haz clic en el botón **"Authorize"** (candado) en la parte superior derecha
2. En el campo "Value", escribe: `Bearer {tu-access-token}`
   - Ejemplo: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
   - **IMPORTANTE**: Debe incluir la palabra "Bearer" seguida de un espacio
3. Haz clic en **"Authorize"**
4. Haz clic en **"Close"**

Ahora todos los endpoints protegidos usarán automáticamente este token.

## Probar Endpoints Protegidos

Una vez configurado el token, puedes probar cualquier endpoint protegido:

1. Busca el endpoint que quieres probar
2. Haz clic en **"Try it out"**
3. Llena los parámetros necesarios
4. Haz clic en **"Execute"**
5. Verás la respuesta con el código de estado y el body

## Características Útiles

### Ver Esquemas

- Haz clic en **"Schemas"** al final de la página
- Verás todos los modelos de datos (DTOs) con sus campos y validaciones
- Útil para entender qué datos enviar/recibir

### Copiar Curl Command

Después de ejecutar una petición:
1. Busca la sección **"Curl"** en la respuesta
2. Copia el comando
3. Puedes ejecutarlo en tu terminal

### Ver Respuestas Posibles

Cada endpoint muestra:
- **Códigos de estado posibles**: 200, 201, 400, 401, etc.
- **Descripción de cada código**: Qué significa cada respuesta
- **Esquema de respuesta**: Estructura del JSON de respuesta

## Flujo de Prueba Completo

### Escenario: Registrar usuario y verificar email

1. **Registrar usuario** (`POST /api/auth/register`)
   - Guarda el `accessToken` de la respuesta
   - Revisa los logs de la aplicación para ver el token de verificación

2. **Verificar email** (`POST /api/auth/verify-email`)
   - Usa el token de verificación de los logs
   - Parámetro: `token=<verification-token>`

3. **Login** (`POST /api/auth/login`)
   - Usa las mismas credenciales
   - Verifica que `isEmailVerified` sea `true`

4. **Refresh token** (`POST /api/auth/refresh`)
   - Usa el `refreshToken` de la respuesta anterior
   - Obtendrás nuevos tokens

## Exportar Especificación OpenAPI

Si quieres usar la especificación en otras herramientas (Postman, Insomnia, etc.):

```bash
# Exportar como JSON
curl http://localhost:8080/v3/api-docs -o openapi.json

# Exportar como YAML
curl http://localhost:8080/v3/api-docs.yaml -o openapi.yaml
```

Luego puedes importar estos archivos en:
- **Postman**: File > Import > Upload Files
- **Insomnia**: Create > Import from File
- **VS Code REST Client**: Usar extensiones que soporten OpenAPI

## Comparación: Swagger UI vs Postman

| Característica | Swagger UI | Postman |
|----------------|------------|---------|
| Documentación integrada | ✅ Sí | ❌ No |
| Probar endpoints | ✅ Sí | ✅ Sí |
| Guardar peticiones | ❌ No | ✅ Sí |
| Variables de entorno | ❌ No | ✅ Sí |
| Colecciones | ❌ No | ✅ Sí |
| Tests automatizados | ❌ No | ✅ Sí |
| Siempre actualizado | ✅ Sí | ❌ Manual |
| Requiere instalación | ❌ No | ✅ Sí |
| Compartir con equipo | ✅ URL | ⚠️ Requiere cuenta |

## Recomendación

**Usa Swagger UI para**:
- Desarrollo rápido y pruebas ad-hoc
- Documentación interactiva para el equipo
- Verificar que los endpoints funcionan correctamente
- Compartir documentación con frontend developers

**Usa Postman para**:
- Tests automatizados y CI/CD
- Colecciones de pruebas complejas
- Pruebas de carga y performance
- Guardar y compartir colecciones de peticiones

## Troubleshooting

### Error 401 (Unauthorized)

- Verifica que configuraste el token JWT correctamente
- Asegúrate de incluir "Bearer " antes del token
- El token expira en 30 minutos, usa refresh token para obtener uno nuevo

### Error 403 (Forbidden)

- El endpoint requiere un rol específico (ADMIN vs CLIENT)
- Verifica que tu usuario tenga los permisos necesarios

### Error 400 (Bad Request)

- Revisa que el JSON esté bien formado
- Verifica que todos los campos requeridos estén presentes
- Revisa las validaciones (longitud, formato, etc.)

### No puedo acceder a Swagger UI

- Verifica que la aplicación esté corriendo en el puerto 8080
- Intenta: http://localhost:8080/swagger-ui/index.html
- Revisa los logs de la aplicación para ver errores

## Recursos Adicionales

- **Swagger UI Docs**: https://swagger.io/tools/swagger-ui/
- **OpenAPI Specification**: https://swagger.io/specification/
- **Spring Boot + OpenAPI**: https://springdoc.org/
