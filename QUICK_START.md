# 🚀 Quick Start - JWT Authentication

## ¿Qué se implementó?

✅ **Sistema de autenticación JWT completo**
- Los endpoints de `/api/v1/auth/**` son públicos
- Todos los demás endpoints requieren token JWT
- Token se obtiene al hacer login o registro
- Token válido por 24 horas

## Archivos Creados

1. **`JwtService.java`** - Genera y valida tokens JWT
2. **`JwtAuthenticationFilter.java`** - Intercepta peticiones y valida tokens
3. **`WebSecurityConfiguration.java`** - Configuración de seguridad actualizada
4. **`AuthController.java`** - Actualizado para devolver token
5. **`AuthResponse.java`** - Actualizado para incluir token

## Cómo Probar

### Opción 1: Con el archivo test-jwt.http

1. Abre `test-jwt.http` en VS Code
2. Instala la extensión "REST Client" si no la tienes
3. Inicia la aplicación: `.\mvnw.cmd spring-boot:run`
4. Ejecuta las peticiones una por una

### Opción 2: Con Postman

1. **Registrar usuario:**
   ```
   POST http://localhost:8080/api/v1/auth/register
   Body (JSON):
   {
     "email": "test@gigmap.com",
     "username": "testuser",
     "password": "password123"
   }
   ```

2. **Copiar el token** de la respuesta

3. **Usar el token en otros endpoints:**
   ```
   GET http://localhost:8080/api/v1/users
   Headers:
   Authorization: Bearer TU_TOKEN_AQUI
   ```

### Opción 3: Con cURL

```bash
# 1. Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"emailOrUsername":"test@gigmap.com","password":"password123"}'

# 2. Guardar el token que recibes

# 3. Usar el token
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## Verificar que funciona

### ✅ Prueba 1: Endpoint público (debe funcionar sin token)
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"emailOrUsername":"test@gigmap.com","password":"password123"}'
```

**Resultado esperado:** 200 OK con token

### ❌ Prueba 2: Endpoint protegido sin token (debe fallar)
```bash
curl -X GET http://localhost:8080/api/v1/users
```

**Resultado esperado:** 401 Unauthorized

### ✅ Prueba 3: Endpoint protegido con token (debe funcionar)
```bash
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

**Resultado esperado:** 200 OK con lista de usuarios

## Iniciar la Aplicación

```bash
.\mvnw.cmd spring-boot:run
```

## Documentación Swagger

Una vez iniciada la aplicación, accede a:
- http://localhost:8080/swagger-ui.html

## Configuración

En `application.properties` puedes cambiar:

```properties
# Duración del token (en milisegundos)
jwt.expiration=86400000

# Clave secreta (¡cambiar en producción!)
jwt.secret=tuClaveSecretaSuperSegura
```

## Estructura del Token JWT

El token contiene:
- `userId` - ID del usuario
- `email` - Email del usuario
- `username` - Username del usuario
- `iat` - Fecha de emisión
- `exp` - Fecha de expiración

## Troubleshooting

### Problema: 401 Unauthorized en todos los endpoints
- **Solución:** Verifica que estés enviando el token en el header `Authorization: Bearer TOKEN`

### Problema: 403 Forbidden
- **Solución:** El token expiró o es inválido. Haz login de nuevo.

### Problema: Token no se genera
- **Solución:** Verifica que la base de datos esté corriendo y el usuario se haya creado correctamente

## Siguientes Pasos

Para producción:
1. ✅ Cambiar `jwt.secret` por una clave segura
2. ✅ Configurar variables de entorno para secretos
3. ✅ Implementar refresh tokens (opcional)
4. ✅ Agregar roles y permisos (opcional)
5. ✅ Configurar HTTPS
