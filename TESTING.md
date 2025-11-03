# Guía Rápida de Testing - HanashiNoMori API

## ⚠️ IMPORTANTE: Sistema de Autenticación Simplificado

**Este proyecto usa un sistema de autenticación simplificado para fines universitarios:**
- ✅ **SIN Spring Security** - Removido para simplificar
- ✅ **Contraseñas en texto plano** - Sin encriptación BCrypt
- ✅ **Comparación directa** - `password.equals(dbPassword)`
- ✅ **Tokens JWT** - Preservados para estructura de API
- ⚠️ **SOLO PARA DEMOS/UNIVERSIDAD** - NO usar en producción

---

## 🚀 Cómo ejecutar el proyecto

### Paso 1: Crear/Limpiar la base de datos

#### Opción A: Crear base de datos desde cero
```bash
# Entra a MySQL
mysql -u root -p

# Ejecuta el script
source database/init.sql

# O copia y pega el contenido del archivo
```

#### Opción B: Limpiar base de datos existente (RECOMENDADO)
```powershell
# Desde PowerShell (una sola línea)
mysql -u root -p -e "DROP DATABASE IF EXISTS hanashinomori; CREATE DATABASE hanashinomori; USE hanashinomori; SOURCE database/init.sql;"
```

#### Opción C: Limpiar solo los datos (mantener estructura)
```sql
-- Desde MySQL
USE hanashinomori;
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE user_roles;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;
SET FOREIGN_KEY_CHECKS = 1;

-- Re-insertar roles
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN'), ('MODERATOR');
```

### Paso 2: Ejecutar la aplicación
```bash
# Opción 1: Con Maven Wrapper (Windows) - RECOMENDADO
.\mvnw.cmd spring-boot:run

# Opción 2: Limpiar y ejecutar
.\mvnw.cmd clean spring-boot:run

# Opción 3: Desde tu IDE
# Click derecho en HanashiNoMoriApplication.java -> Run
```

### Paso 3: Verificar que funcione
```bash
# La aplicación debe estar corriendo en:
http://localhost:8080

# Debes ver en la consola:
# Started HanashiNoMoriApplication in X seconds
```

---

## 📡 Pruebas con cURL (Windows PowerShell)

### 1️⃣ REGISTRO DE USUARIO (Sistema Simplificado)

```powershell
# Registrar nuevo usuario con contraseña simple
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{
    username = "testuser"
    email = "test@test.com"
    password = "123456"
    fullName = "Usuario de Prueba"
} | ConvertTo-Json)
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "id": 1,
    "username": "testuser",
    "email": "test@test.com",
    "roles": ["USER"],
    "createdAt": "2025-11-02T20:10:02"
  }
}
```

**⚠️ NOTA:** La contraseña "123456" se guarda en **texto plano** en la base de datos.

---

### 2️⃣ LOGIN (Comparación Simple)

```powershell
# Login con contraseña en texto plano
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{
    username = "testuser"
    password = "123456"
} | ConvertTo-Json)

# Guardar el token en una variable
$token = $response.data.token

# Mostrar el token
Write-Host "Token guardado: $token"
```

**¿Cómo funciona?**
```java
// Backend: SimpleAuthService.java
if (!user.getPassword().equals(request.getPassword())) {
    throw new RuntimeException("Usuario o contraseña incorrectos");
}
// Comparación directa: "123456".equals("123456") ✅
```

---

### 3️⃣ REFRESH TOKEN

```powershell
# Refrescar token (usa el refreshToken de la respuesta anterior)
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/refresh" -Method POST -Headers @{
    "Authorization" = "Bearer $($response.data.refreshToken)"
}
```

---

### 4️⃣ LOGOUT

```powershell
# Cerrar sesión (requiere token)
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/logout" -Method POST -Headers @{
    "Authorization" = "Bearer $token"
}
```

---

## 🧪 Pruebas con Postman

### Configuración inicial

1. **Crear nueva Collection**: `HanashiNoMori API`

2. **Configurar variable de entorno**:
   - Variable: `baseUrl`
   - Valor: `http://localhost:8080`

### Request 1: Register (Sistema Simplificado)

```
POST {{baseUrl}}/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@test.com",
  "password": "123456",
  "fullName": "Usuario de Prueba"
}
```

**Test Script (pestaña Tests):**
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("access_token", jsonData.data.token);
    pm.environment.set("refresh_token", jsonData.data.refreshToken);
    console.log("✅ Token guardado:", jsonData.data.token);
    console.log("⚠️ Contraseña guardada en texto plano (no BCrypt)");
}
```

### Request 2: Login (Comparación Simple)

```
POST {{baseUrl}}/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

**Test Script:**
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("access_token", jsonData.data.token);
    pm.environment.set("refresh_token", jsonData.data.refreshToken);
    console.log("✅ Login exitoso - Comparación directa de strings");
}
```

### Request 3: Refresh Token

```
POST {{baseUrl}}/api/auth/refresh
Authorization: Bearer {{refresh_token}}
```

### Request 4: Logout

```
POST {{baseUrl}}/api/auth/logout
Authorization: Bearer {{access_token}}
```

---

## 🔍 Verificar en MySQL

```sql
USE hanashinomori;

-- Ver usuarios registrados (VERÁS CONTRASEÑAS EN TEXTO PLANO)
SELECT id, username, email, password, created_at FROM users;
-- Resultado esperado:
-- +----+----------+---------------+----------+---------------------+
-- | id | username | email         | password | created_at          |
-- +----+----------+---------------+----------+---------------------+
-- | 1  | testuser | test@test.com | 123456   | 2025-11-02 20:10:02 |
-- +----+----------+---------------+----------+---------------------+

-- Ver roles asignados
SELECT u.username, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id;

-- Verificar que NO hay Spring Security
-- (Las contraseñas NO comienzan con $2a$ o $2b$)
SELECT username, 
       CASE 
           WHEN password LIKE '$2a$%' THEN '❌ BCrypt (sistema anterior)'
           ELSE '✅ Texto plano (sistema actual)'
       END as tipo_password
FROM users;

-- Ver todas las tablas
SHOW TABLES;

-- Limpiar base de datos completa
DROP DATABASE IF EXISTS hanashinomori;
CREATE DATABASE hanashinomori;
USE hanashinomori;
SOURCE database/init.sql;
```

---

## 🐛 Troubleshooting

### Error: "Access denied for user 'root'@'localhost'"
**Solución:** Actualiza la contraseña en `application.properties`
```properties
spring.datasource.password=tu_password
```

### Error: "Unknown database 'hanashinomori'"
**Solución:** Crea la base de datos primero
```bash
mysql -u root -p -e "CREATE DATABASE hanashinomori; USE hanashinomori; SOURCE database/init.sql;"
```

### Error: "Port 8080 already in use"
**Solución:** Cambia el puerto en `application.properties` o mata el proceso
```powershell
# Ver qué proceso usa el puerto 8080
netstat -ano | findstr :8080

# Matar el proceso (reemplaza PID con el número que viste)
taskkill /PID <PID> /F
```

### Error: "Unable to start embedded Tomcat"
**Solución:** Verifica que tienes Java 17 instalado
```bash
java -version
# Debe mostrar: openjdk version "17.0.x"
```

### Error: "Bad credentials" o "Usuario o contraseña incorrectos"
**Causas posibles:**
1. **Contraseña BCrypt antigua**: Si migraste desde el sistema anterior, algunos usuarios pueden tener contraseñas encriptadas
   ```sql
   -- Ver tipo de contraseña
   SELECT username, password FROM users;
   -- Si empieza con $2a$ es BCrypt (sistema antiguo)
   
   -- Solución: Limpiar base de datos
   DROP DATABASE hanashinomori;
   CREATE DATABASE hanashinomori;
   SOURCE database/init.sql;
   ```

2. **Contraseña incorrecta**: El sistema actual compara strings exactos
   ```java
   // "123456".equals("123456") ✅
   // "123456".equals("12345")  ❌
   ```

### Error: Compilación con Java 21
**Solución:** Forzar Java 17
```powershell
.\mvnw.cmd clean compile -Dmaven.compiler.source=17 -Dmaven.compiler.target=17
```

---

## 📊 Logs útiles

Los logs se muestran en la consola cuando ejecutas la aplicación. Busca:

✅ **Startup exitoso:**
```
Started HanashiNoMoriApplication in X seconds (process running for Y)
```

✅ **Registro exitoso (Sistema Simplificado):**
```
=== REGISTRO SIMPLE: testuser ===
Usuario guardado con contraseña en texto plano
```

✅ **Login exitoso (Comparación Simple):**
```
=== LOGIN SIMPLE: testuser ===
Contraseña correcta
```

❌ **Error de autenticación:**
```
Usuario o contraseña incorrectos
```

⚠️ **Advertencia sobre Spring Security:**
```
// NO deberías ver estos logs (Spring Security removido):
❌ "Using generated security password"
❌ "DaoAuthenticationProvider"
❌ "SecurityFilterChain"
```

---

## 🔐 Diferencias: Sistema Anterior vs Actual

| Característica | Sistema Anterior | Sistema Actual |
|----------------|------------------|----------------|
| **Framework de Seguridad** | Spring Security 6.5.5 | ❌ Removido |
| **Encriptación** | BCrypt ($2a$10$...) | ❌ Texto plano |
| **Comparación Password** | `matches()` method | `equals()` method |
| **AuthenticationManager** | ✅ Sí | ❌ No |
| **DaoAuthenticationProvider** | ✅ Sí | ❌ No |
| **SecurityFilterChain** | ✅ Sí | ❌ No |
| **UserDetailsService** | ✅ Sí | ❌ No |
| **Archivos de Configuración** | SecurityConfig.java | SimpleConfig.java |
| **Servicio de Auth** | AuthService.java | SimpleAuthService.java |
| **Complejidad** | Alta (28 archivos) | Baja (24 archivos) |
| **Para Producción** | ✅ Sí | ❌ No |
| **Para Universidad** | ⚠️ Complejo | ✅ Perfecto |

---

## 💡 ¿Por qué este sistema simplificado?

**Para proyectos universitarios:**
- ✅ Más fácil de entender y explicar
- ✅ Menos código para revisar
- ✅ Menos posibilidades de errores
- ✅ Enfoque en lógica de negocio, no en seguridad
- ✅ Ideal para demos y presentaciones

**⚠️ IMPORTANTE:** 
Este sistema **NUNCA** debe usarse en producción. Para aplicaciones reales, **siempre** usa:
- Spring Security
- Encriptación BCrypt (mínimo)
- HTTPS
- Validación de tokens
- Rate limiting
- Y muchas otras medidas de seguridad

---

## 📝 Próximos endpoints a implementar

Una vez que la autenticación funcione correctamente, los siguientes pasos son:

1. **MediaController** - CRUD de medios (libros, mangas, etc.)
2. **LibraryController** - Gestión de biblioteca personal
3. **ReviewController** - Sistema de reseñas
4. **CollectionController** - Colecciones personalizadas
5. **NotificationController** - Sistema de notificaciones

---

## 🎯 Checklist de testing

### Configuración Inicial
- [ ] Base de datos `hanashinomori` creada
- [ ] Tablas creadas correctamente (14 tablas)
- [ ] Roles insertados (USER, ADMIN, MODERATOR)
- [ ] Aplicación corriendo en http://localhost:8080
- [ ] Java 17 configurado correctamente

### Autenticación Simplificada
- [ ] Registro de usuario funciona correctamente
- [ ] Contraseña se guarda en **texto plano** (sin $2a$)
- [ ] Login devuelve token JWT válido
- [ ] Comparación de contraseña es directa (`equals()`)
- [ ] Refresh token funciona
- [ ] Logout limpia la sesión
- [ ] Rol "USER" se asigna automáticamente
- [ ] No se permite registrar usuarios duplicados

### Validaciones
- [ ] Email inválido retorna error 400
- [ ] Campos vacíos retornan error 400
- [ ] Username duplicado retorna error 400
- [ ] Login con contraseña incorrecta retorna error 401

### Verificación en Base de Datos
- [ ] Usuarios se crean con ID autoincremental
- [ ] Contraseñas visibles en texto plano (⚠️ solo para universidad)
- [ ] Relación user_roles funciona correctamente
- [ ] Timestamps (created_at, updated_at) se generan automáticamente

### Sistema Simplificado Confirmado
- [ ] ❌ NO hay logs de Spring Security
- [ ] ❌ NO hay BCrypt en las contraseñas
- [ ] ❌ NO hay AuthenticationManager
- [ ] ✅ SimpleAuthService se usa correctamente
- [ ] ✅ Comparación directa de strings funciona

---

## 📝 Ejemplos de Pruebas Rápidas

### Test 1: Registro + Login inmediato
```powershell
# 1. Registrar
$reg = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{username="demo1"; email="demo1@test.com"; password="pass123"; fullName="Demo User"} | ConvertTo-Json)

# 2. Login inmediato con las mismas credenciales
$login = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{username="demo1"; password="pass123"} | ConvertTo-Json)

# 3. Verificar que ambos funcionaron
Write-Host "✅ Registro: Token obtenido" -ForegroundColor Green
Write-Host "✅ Login: Token obtenido" -ForegroundColor Green
```

### Test 2: Verificar contraseña en texto plano
```sql
-- En MySQL
USE hanashinomori;
SELECT username, password, 
       CASE 
           WHEN password LIKE '$2a$%' THEN '❌ FALLO - Tiene BCrypt'
           ELSE '✅ CORRECTO - Texto plano'
       END as estado
FROM users;
```

### Test 3: Login con contraseña incorrecta
```powershell
# Debe retornar error 401
try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{username="demo1"; password="INCORRECTA"} | ConvertTo-Json)
} catch {
    Write-Host "✅ CORRECTO - Login rechazado con contraseña incorrecta" -ForegroundColor Green
}
```

---

**¡Listo para empezar! 🚀**
