# JWT Authentication - GigMap API

## Descripción General

El sistema de autenticación JWT protege todos los endpoints de la API **excepto** los de autenticación (`/api/v1/auth/**`).

## Endpoints Públicos (Sin token requerido)

- `POST /api/v1/auth/register` - Registrar nuevo usuario
- `POST /api/v1/auth/login` - Iniciar sesión
- `/swagger-ui/**` - Documentación Swagger

## Endpoints Protegidos (Token requerido)

Todos los demás endpoints requieren un token JWT válido:
- `/api/v1/users/**`
- `/api/v1/concerts/**`
- Cualquier otro endpoint futuro

## Flujo de Autenticación

### 1. Registro de Usuario

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "username": "usuario123",
  "password": "miPassword123"
}
```

**Respuesta exitosa (201):**
```json
{
  "id": 1,
  "email": "usuario@ejemplo.com",
  "username": "usuario123",
  "isArtist": false,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "User registered successfully"
}
```

### 2. Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "emailOrUsername": "usuario@ejemplo.com",
  "password": "miPassword123"
}
```

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "email": "usuario@ejemplo.com",
  "username": "usuario123",
  "isArtist": false,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

### 3. Usar el Token en Peticiones Protegidas

Incluye el token en el header `Authorization` con el prefijo `Bearer `:

```http
GET /api/v1/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Ejemplos con cURL

### Registro
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "username": "testuser",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "emailOrUsername": "test@example.com",
    "password": "password123"
  }'
```

### Acceder a endpoint protegido
```bash
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

## Ejemplos con JavaScript/Fetch

```javascript
// 1. Login
const loginResponse = await fetch('http://localhost:8080/api/v1/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    emailOrUsername: 'test@example.com',
    password: 'password123'
  })
});

const { token } = await loginResponse.json();

// 2. Guardar token (localStorage, sessionStorage, etc.)
localStorage.setItem('token', token);

// 3. Usar token en peticiones
const usersResponse = await fetch('http://localhost:8080/api/v1/users', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const users = await usersResponse.json();
```

## Configuración

### Variables de Entorno (opcional)

Puedes configurar estas propiedades en `application.properties`:

```properties
# Clave secreta para firmar tokens (cambiar en producción)
jwt.secret=mySecretKeyForJWTTokenGeneration12345

# Tiempo de expiración en milisegundos (24 horas por defecto)
jwt.expiration=86400000
```

## Códigos de Respuesta

- **200** - Login/operación exitosa
- **201** - Usuario registrado exitosamente
- **400** - Datos inválidos o usuario ya existe
- **401** - No autenticado o credenciales inválidas
- **403** - Token expirado o inválido
- **404** - Recurso no encontrado

## Seguridad

- Los tokens tienen una duración de **24 horas** por defecto
- Las contraseñas se hashean con **BCrypt**
- Los tokens se validan en cada petición a endpoints protegidos
- **IMPORTANTE**: En producción, cambia la clave secreta `jwt.secret` por una segura

## Componentes Implementados

1. **JwtService** - Genera y valida tokens JWT
2. **JwtAuthenticationFilter** - Intercepta peticiones y valida tokens
3. **WebSecurityConfiguration** - Configura qué endpoints están protegidos
4. **AuthController** - Maneja registro y login
5. **AuthResponse** - Incluye el token en la respuesta
