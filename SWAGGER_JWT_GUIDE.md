# 🔐 Guía: Usar JWT en Swagger UI

## 📋 Pasos Simples

### 1️⃣ Inicia tu aplicación
```bash
.\mvnw.cmd spring-boot:run
```

### 2️⃣ Abre Swagger UI
Navega a: **http://localhost:8080/swagger-ui.html**

### 3️⃣ Haz Login para obtener el token

1. Ve al endpoint **`POST /api/v1/auth/login`**
2. Click en **"Try it out"**
3. Ingresa tus credenciales:
   ```json
   {
     "emailOrUsername": "tu_usuario",
     "password": "tu_password"
   }
   ```
4. Click en **"Execute"**
5. **COPIA EL TOKEN** de la respuesta (el valor completo del campo `token`)

### 4️⃣ Autoriza Swagger con el token

1. **Busca el botón "Authorize" 🔓** (arriba a la derecha en Swagger UI)
2. **Click en "Authorize"**
3. Se abrirá un popup con el campo "bearerAuth"
4. **Pega tu token** en el campo "Value"
   - ⚠️ **NO agregues "Bearer "** - Swagger lo hace automáticamente
   - Solo pega el token directamente
5. Click en **"Authorize"**
6. Click en **"Close"**

### 5️⃣ ¡Listo! Ahora puedes usar todos los endpoints 🎉

Ahora verás que:
- El botón cambió a **"Authorize" 🔒** (con candado cerrado)
- Todos los endpoints protegidos funcionarán automáticamente
- No necesitas agregar el header manualmente en cada petición

---

## 🖼️ Vista del Swagger UI

Verás algo así:

```
╔════════════════════════════════════════════════╗
║  GigMap-API-V1                 [Authorize 🔓]  ║
╠════════════════════════════════════════════════╣
║                                                ║
║  Authentication                                ║
║  ▼ POST /api/v1/auth/register                 ║
║  ▼ POST /api/v1/auth/login      ← USA ESTE    ║
║                                                ║
║  Users                                         ║
║  ▼ GET  /api/v1/users           🔒            ║
║  ▼ GET  /api/v1/users/{userId}  🔒            ║
║  ▼ PUT  /api/v1/users/{userId}  🔒            ║
║                                                ║
╚════════════════════════════════════════════════╝
```

Después de autorizar:

```
╔════════════════════════════════════════════════╗
║  GigMap-API-V1                 [Authorize 🔒]  ║
╠════════════════════════════════════════════════╣
║  ✅ Token activo - Endpoints desbloqueados    ║
╚════════════════════════════════════════════════╝
```

---

## 🎯 Ejemplo Completo Paso a Paso

### Ejemplo con usuario nuevo:

**Paso 1: Registrar usuario**
```
POST /api/v1/auth/register
{
  "email": "test@gigmap.com",
  "username": "testuser",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "id": 1,
  "email": "test@gigmap.com",
  "username": "testuser",
  "isArtist": false,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaWdtYXAtYXBpIiwic3ViIjoiMSIsImVtYWlsIjoidGVzdEBnaWdtYXAuY29tIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTY5ODc2NTQzMiwiZXhwIjoxNjk4ODUxODMyfQ.xyz123...",
  "message": "User registered successfully"
}
```

**Paso 2: Copiar el token**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaWdtYXAtYXBpIiwic3ViIjoiMSIsImVtYWlsIjoidGVzdEBnaWdtYXAuY29tIiwidXNlcm5hbWUiOiJ0ZXN0dXNlciIsImlhdCI6MTY5ODc2NTQzMiwiZXhwIjoxNjk4ODUxODMyfQ.xyz123...
```

**Paso 3: Autorizar en Swagger**
- Click en "Authorize" 🔓
- Pegar el token
- Click en "Authorize"
- Click en "Close"

**Paso 4: Probar endpoint protegido**
```
GET /api/v1/users
```
✅ **Funciona!** - Retorna la lista de usuarios

---

## ❌ Errores Comunes

### Error: "401 Unauthorized"
**Causa:** No has autorizado o el token no está configurado
**Solución:** Sigue los pasos 1-4 arriba

### Error: "403 Forbidden"
**Causa:** Token expiró o es inválido
**Solución:** Haz login nuevamente y obtén un token nuevo

### Error: No aparece el botón "Authorize"
**Causa:** La app no se reinició después de cambiar la configuración
**Solución:** Reinicia la aplicación

---

## 🔄 Para Cerrar Sesión en Swagger

1. Click en **"Authorize" 🔒**
2. Click en **"Logout"**
3. El botón volverá a **"Authorize" 🔓**

---

## 💡 Tips

- ✅ El token dura **24 horas**
- ✅ Puedes copiar el token desde register o login
- ✅ No necesitas agregar "Bearer " manualmente
- ✅ Una vez autorizado, TODOS los endpoints protegidos funcionan
- ✅ Puedes ver el candado 🔒 al lado de endpoints protegidos

---

## 🚀 ¡Eso es todo!

Ahora puedes probar todos tus endpoints directamente desde Swagger sin necesidad de Postman o cURL.
