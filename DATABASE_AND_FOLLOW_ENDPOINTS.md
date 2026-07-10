# Documentación de Base de Datos y Endpoints de Follow/Unfollow

## Base de Datos

### Tabla: `artist_followers`

Esta tabla gestiona la relación entre fans (usuarios) y artistas que siguen.

```sql
CREATE TABLE public.artist_followers (
  fan_id bigint NOT NULL,
  artist_id bigint NOT NULL,
  created_at timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT artist_followers_pkey PRIMARY KEY (fan_id, artist_id),
  CONSTRAINT fk_artist_followers_fan FOREIGN KEY (fan_id) REFERENCES public.users(id),
  CONSTRAINT fk_artist_followers_artist FOREIGN KEY (artist_id) REFERENCES public.users(id)
);
```

**Campos:**
- `fan_id` (BIGINT, PRIMARY KEY): ID del usuario que sigue (fan)
- `artist_id` (BIGINT, PRIMARY KEY): ID del artista siendo seguido
- `created_at` (TIMESTAMP): Fecha de creación del registro (por defecto: ahora)

**Restricciones:**
- Clave primaria compuesta: `(fan_id, artist_id)` - garantiza que un usuario no pueda seguir al mismo artista dos veces
- Clave foránea: `fan_id` referencia a `users(id)` - el fan debe existir en la tabla usuarios
- Clave foránea: `artist_id` referencia a `users(id)` - el artista debe existir en la tabla usuarios

---

### Tabla: `user_following_artists`

Esta es una tabla alternativa que también gestiona la relación de seguimiento.

```sql
CREATE TABLE public.user_following_artists (
  fan_id bigint NOT NULL,
  artist_id bigint NOT NULL,
  CONSTRAINT fkdtw1sple9iyqjpj4h8e4a9qef FOREIGN KEY (artist_id) REFERENCES public.users(id),
  CONSTRAINT fkejxdvmtph1u6soxe9s6t975pq FOREIGN KEY (fan_id) REFERENCES public.users(id)
);
```

**Campos:**
- `fan_id` (BIGINT): ID del usuario que sigue
- `artist_id` (BIGINT): ID del artista siendo seguido

**Nota:** Esta tabla se usa en la entidad JPA `User` a través de la anotación `@ManyToMany`.

---

### Tabla: `users`

Tabla principal de usuarios con información de perfil.

```sql
CREATE TABLE public.users (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  created_at timestamp without time zone NOT NULL,
  updated_at timestamp without time zone NOT NULL,
  descripcion text,
  email character varying NOT NULL UNIQUE,
  imagen_url character varying,
  name character varying,
  password_hash character varying,
  role character varying NOT NULL CHECK (role::text = ANY (ARRAY['FAN'::character varying, 'ARTIST'::character varying]::text[])),
  username character varying NOT NULL UNIQUE,
  followers_count integer NOT NULL DEFAULT 0,
  CONSTRAINT users_pkey PRIMARY KEY (id)
);
```

**Campos Relevantes:**
- `id` (BIGINT): Identificador único del usuario
- `role` (VARCHAR): Rol del usuario ('FAN' o 'ARTIST')
- `followers_count` (INTEGER): Contador de seguidores
- `email`, `username`: Campos únicos para identificación

---

## Endpoints REST - Follow/Unfollow de Artistas

Base URL: `http://localhost:8080/api/v1/users`

### 1. Seguir a un Artista

**Endpoint:** `PUT /api/v1/users/{userId}/follow/{artistId}`

**Descripción:** Un usuario (fan) sigue a un artista.

**Parámetros:**
- `userId` (path): ID del usuario que quiere seguir
- `artistId` (path): ID del artista a seguir

**Respuesta Exitosa (200):**
```
No body - vacío
```

**Respuesta Error (400):**
```json
{
  "error": "Fan or artist not found"
}
```

**Ejemplo cURL:**
```bash
curl -X PUT "http://localhost:8080/api/v1/users/10/follow/42"
```

**Ejemplo con HTTPie:**
```bash
http PUT http://localhost:8080/api/v1/users/10/follow/42
```

**Ejemplo con Postman:**
- Método: PUT
- URL: `http://localhost:8080/api/v1/users/10/follow/42`
- Body: vacío

---

### 2. Dejar de Seguir a un Artista

**Endpoint:** `PUT /api/v1/users/{userId}/unfollow/{artistId}`

**Descripción:** Un usuario deja de seguir a un artista.

**Parámetros:**
- `userId` (path): ID del usuario que deja de seguir
- `artistId` (path): ID del artista a dejar de seguir

**Respuesta Exitosa (200):**
```
No body - vacío
```

**Respuesta Error (400):**
```json
{
  "error": "Fan or artist not found"
}
```

**Ejemplo cURL:**
```bash
curl -X PUT "http://localhost:8080/api/v1/users/10/unfollow/42"
```

**Ejemplo con HTTPie:**
```bash
http PUT http://localhost:8080/api/v1/users/10/unfollow/42
```

**Ejemplo con Postman:**
- Método: PUT
- URL: `http://localhost:8080/api/v1/users/10/unfollow/42`
- Body: vacío

---

### 3. Verificar si un Usuario Sigue a un Artista

**Endpoint:** `GET /api/v1/users/{userId}/following/{artistId}`

**Descripción:** Retorna si el usuario está siguiendo al artista.

**Parámetros:**
- `userId` (path): ID del usuario
- `artistId` (path): ID del artista a verificar

**Respuesta Exitosa (200):**
```
true
```
o
```
false
```

**Respuesta Error (404):**
```
No body - usuario o artista no existen
```

**Ejemplo cURL:**
```bash
curl -X GET "http://localhost:8080/api/v1/users/10/following/42"
```

**Ejemplo con HTTPie:**
```bash
http GET http://localhost:8080/api/v1/users/10/following/42
```

**Ejemplo con Postman:**
- Método: GET
- URL: `http://localhost:8080/api/v1/users/10/following/42`
- Body: vacío

---

## Arquitectura de Implementación

Los endpoints han sido implementados siguiendo la arquitectura hexagonal del proyecto:

### 1. **Domain Layer** (Capa de Dominio)
- **Agregados**: `User` con métodos `follow()`, `unfollow()`, `isFollowing()`
- **Comandos**: `FollowArtistCommand`, `UnfollowArtistCommand`
- **Queries**: `IsUserFollowingArtistQuery`
- **Servicios**: `UserCommandService`, `UserQueryService`

### 2. **Application Layer** (Capa de Aplicación)
- **Implementaciones de Servicios**:
  - `UserCommandServiceImpl`: maneja los comandos de follow/unfollow
  - `UserQueryServiceImpl`: maneja las queries de verificación

### 3. **Infrastructure Layer** (Capa de Infraestructura)
- **Repositorios**: `UserRepository` (acceso a datos)
- **Persistencia JPA**: mapeo de la relación ManyToMany en la tabla `user_following_artists`

### 4. **Interface Layer** (Capa de Interfaz)
- **Controllers**: `UsersController` expone los tres endpoints REST
- **Recursos**: Transformadores de datos entre Request/Response y Entities

---

## Flujo de Ejecución

### Seguir a un Artista (Follow):
1. Cliente hace `PUT /api/v1/users/{userId}/follow/{artistId}`
2. `UsersController.followArtist()` crea un `FollowArtistCommand`
3. `UserCommandService.handle()` procesa el comando
4. `UserCommandServiceImpl` obtiene fan y artist del repositorio
5. Llama al método `fan.follow(artist)` en la entidad User
6. Guarda el fan actualizado en la base de datos
7. Retorna 200 OK si es exitoso, 400 si no encuentra fan o artist

### Dejar de Seguir (Unfollow):
1. Cliente hace `PUT /api/v1/users/{userId}/unfollow/{artistId}`
2. Similar al flujo de follow pero llama a `fan.unfollow(artist)`
3. Retorna 200 OK o 400 según corresponda

### Verificar Seguimiento (Is Following):
1. Cliente hace `GET /api/v1/users/{userId}/following/{artistId}`
2. `UsersController.isFollowingArtist()` crea un `IsUserFollowingArtistQuery`
3. `UserQueryService.handle()` procesa la query
4. `UserQueryServiceImpl` verifica usando `fan.isFollowing(artist)`
5. Retorna `true` o `false` si ambos usuarios existen
6. Retorna `false` si alguno de los usuarios no existe

---

## Validaciones

- **Validación de Existencia**: Se verifica que ambos usuarios (fan y artist) existan antes de procesar
- **Validación de Duplicados**: El método `follow()` evita agregar duplicados usando `contains()`
- **Validación de Auto-seguimiento**: El método `follow()` lanza una excepción si un usuario intenta seguirse a sí mismo
- **Validación de Null**: Se valida que los parámetros no sean nulos

---

## Próximos Pasos Sugeridos

1. **Agregar contar seguidores**: Implementar endpoint para obtener número de seguidores de un artista
2. **Listar seguidores**: Endpoint `GET /api/v1/users/{artistId}/followers` para listar todos los seguidores
3. **Listar siguiendo**: Endpoint `GET /api/v1/users/{userId}/following` para listar todos los artistas que sigue
4. **Notificaciones**: Enviar notificaciones cuando alguien sigue a un artista
5. **Rate Limiting**: Agregar límites de rate para los endpoints
6. **Auditoría**: Registrar todos los eventos de follow/unfollow para análisis

---

## Testing

### Unit Tests Sugeridos

```java
@Test
public void testFollowArtist() {
    // Given
    Long fanId = 1L;
    Long artistId = 2L;
    
    // When
    userCommandService.handle(new FollowArtistCommand(fanId, artistId));
    
    // Then
    assertTrue(userQueryService.handle(new IsUserFollowingArtistQuery(fanId, artistId)));
}

@Test
public void testUnfollowArtist() {
    // Given
    Long fanId = 1L;
    Long artistId = 2L;
    
    // When
    userCommandService.handle(new FollowArtistCommand(fanId, artistId));
    userCommandService.handle(new UnfollowArtistCommand(fanId, artistId));
    
    // Then
    assertFalse(userQueryService.handle(new IsUserFollowingArtistQuery(fanId, artistId)));
}

@Test
public void testFollowNonExistentArtist() {
    // Given
    Long fanId = 1L;
    Long artistId = 999L;
    
    // When & Then
    assertThrows(IllegalArgumentException.class, 
        () -> userCommandService.handle(new FollowArtistCommand(fanId, artistId)));
}
```

---

## Notas de Implementación

- Los endpoints retornan `204 No Content` podría ser más RESTful que `200 OK` para PUT operations sin cuerpo
- Se podría agregar un `Location` header con la URL del recurso creado
- La query `IsUserFollowingArtistQuery` retorna `false` si alguno de los usuarios no existe en lugar de lanzar excepción para una UX mejor
- Los comandos usan transacciones `@Transactional` para garantizar consistency en base de datos

