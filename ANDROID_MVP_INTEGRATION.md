# 📱 Backend Adaptado para Android - Guía Completa

## 🎯 **LO QUE SE HIZO**

He adaptado el **backend Spring Boot** para que funcione PERFECTAMENTE con tu **app Android MVP** sin tocar casi nada del código Android.

---

## ✅ **CAMBIOS REALIZADOS EN EL BACKEND**

### 1. **Nuevos DTOs Simples (MVP)**

Creé 4 nuevos DTOs que coinciden EXACTAMENTE con lo que espera Android:

#### `SimpleRegisterRequest.java`
```java
{
  "username": "test",
  "email": "test@test.com",
  "password": "123456"
}
```

#### `SimpleRegisterResponse.java`
```java
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "userId": 1
}
```

#### `SimpleLoginRequest.java`
```java
{
  "username": "test",
  "password": "123456"
}
```

#### `SimpleLoginResponse.java`
```java
{
  "success": true,
  "message": "Login exitoso",
  "userId": 1,
  "username": "test"
}
```

### 2. **Nuevo Controller: `AndroidAuthController.java`**

Creé un controller completamente nuevo con endpoints que coinciden con los de tu app Android:

| Endpoint Android | Endpoint Backend | Método |
|------------------|------------------|--------|
| `POST /users` | `POST /users` | Registro |
| `POST /users/login` | `POST /users/login` | Login |
| `GET /users/ping` | `GET /users/ping` | Test |

**Características:**
- ✅ Sin JWT tokens (MVP simple)
- ✅ Contraseñas en texto plano (para universidad)
- ✅ Respuestas simples (solo userId, username, success, message)
- ✅ CORS habilitado (`@CrossOrigin`)
- ✅ Logs detallados para debugging

---

## ✅ **CAMBIOS MÍNIMOS EN ANDROID (Solo 2 archivos)**

### 1. **`RetrofitProvider.kt`** - Cambio de URL

```kotlin
// ANTES (JSONPlaceholder - API falsa)
.baseUrl("https://jsonplaceholder.typicode.com/")

// DESPUÉS (Tu backend local)
.baseUrl("http://10.0.2.2:8080/")  // Para emulador
// .baseUrl("http://TU_IP:8080/")  // Para dispositivo físico
```

### 2. **`AndroidManifest.xml`** - Permisos HTTP

```xml
<!-- Agregado -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
android:usesCleartextTraffic="true"
```

---

## 🚀 **CÓMO PROBAR LA CONEXIÓN**

### **Paso 1: Verificar que el Backend esté corriendo**

```powershell
# En terminal de VSCode
cd "c:\Users\darks\Documents\Proyectos Duoc\Aplicacione Moviles\HanashiNoMori-VSC"
.\mvnw.cmd spring-boot:run
```

**Espera ver:**
```
Started HanashiNoMoriApplication in X seconds (process running for Y)
Tomcat started on port 8080 (http)
```

### **Paso 2: Test rápido con PowerShell**

```powershell
# Test de ping
Invoke-RestMethod -Uri "http://localhost:8080/users/ping"
# Debe devolver: "Backend Android MVP funcionando correctamente ✅"

# Test de registro
Invoke-RestMethod -Uri "http://localhost:8080/users" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{username="testmvp"; email="test@mvp.com"; password="123456"} | ConvertTo-Json)

# Test de login
Invoke-RestMethod -Uri "http://localhost:8080/users/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{username="testmvp"; password="123456"} | ConvertTo-Json)
```

**Respuestas esperadas:**

**Registro exitoso:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "userId": 14
}
```

**Login exitoso:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "userId": 14,
  "username": "testmvp"
}
```

### **Paso 3: Configurar Android según tu dispositivo**

#### **Si usas EMULADOR Android:**
No cambies nada, la URL ya está correcta:
```kotlin
// RetrofitProvider.kt
.baseUrl("http://10.0.2.2:8080/")
```

#### **Si usas DISPOSITIVO FÍSICO:**

1. **Obtén tu IP local:**
```powershell
ipconfig
# Busca "IPv4 Address" → Ejemplo: 192.168.1.105
```

2. **Actualiza RetrofitProvider.kt:**
```kotlin
.baseUrl("http://192.168.1.105:8080/")  // USA TU IP REAL
```

### **Paso 4: Ejecutar la App Android**

1. Abre Android Studio
2. **Build → Rebuild Project**
3. **Run → Run 'app'**
4. Selecciona tu emulador o dispositivo

### **Paso 5: Probar Registro en la App**

1. Abre la pantalla de registro
2. Ingresa datos:
   - **Username:** `androiduser`
   - **Email:** `android@test.com`
   - **Password:** `123456`
3. Click en **Registrarse**

**¿Qué debería pasar?**
- ✅ Alert dialog: "¡Registro Exitoso!"
- ✅ Redirección a pantalla de login

### **Paso 6: Probar Login en la App**

1. Ingresa:
   - **Username:** `androiduser`
   - **Password:** `123456`
2. Click en **Iniciar Sesión**

**¿Qué debería pasar?**
- ✅ Login exitoso
- ✅ Redirección a HomeScreen

---

## 🔍 **DEBUGGING: Ver Logs en Tiempo Real**

### **Logs del Backend (VSCode)**

Mientras el backend corre, verás logs como:
```
INFO  c.H.H.Controller.AndroidAuthController : === REGISTRO ANDROID MVP: androiduser ===
INFO  c.H.H.Controller.AndroidAuthController : Usuario guardado con ID: 14
INFO  c.H.H.Controller.AndroidAuthController : === REGISTRO EXITOSO: androiduser ===
```

### **Logs de Android (Logcat en Android Studio)**

Filtra por **"OkHttp"** para ver las requests:
```
D/OkHttp: --> POST http://10.0.2.2:8080/users
D/OkHttp: {"username":"androiduser","email":"android@test.com","password":"123456"}
D/OkHttp: <-- 201 Created
D/OkHttp: {"success":true,"message":"Usuario registrado exitosamente","userId":14}
```

---

## 📊 **DIAGRAMA DE FLUJO**

```
┌─────────────────────┐
│   Android App       │
│   (Jetpack Compose) │
└──────────┬──────────┘
           │
           │ HTTP POST /users
           │ { username, email, password }
           ▼
┌─────────────────────┐
│  RetrofitProvider   │
│  10.0.2.2:8080      │
└──────────┬──────────┘
           │
           │ Retrofit + OkHttp
           ▼
┌──────────────────────────────┐
│  Backend Spring Boot         │
│  AndroidAuthController.java  │
│  Port: 8080                  │
└──────────┬───────────────────┘
           │
           │ 1. Validar usuario no existe
           │ 2. Guardar en MySQL
           │ 3. Asignar rol USER
           ▼
┌─────────────────────┐
│   MySQL Database    │
│   hanashinomori     │
│   - users table     │
│   - roles table     │
└─────────────────────┘
           │
           │ Response
           ▼
┌──────────────────────┐
│  Android App         │
│  { success, userId } │
│  → HomeScreen        │
└──────────────────────┘
```

---

## 🐛 **TROUBLESHOOTING**

### **Error: "Unable to resolve host"**

**Causa:** Android no puede conectarse al backend

**Soluciones:**
1. ✅ Verifica que el backend esté corriendo (ve al navegador: `http://localhost:8080/users/ping`)
2. ✅ Si usas emulador, usa `10.0.2.2` (NO `localhost`)
3. ✅ Si usas dispositivo físico, usa tu IP local (ejemplo: `192.168.1.105`)
4. ✅ Verifica que ambos (PC y móvil) estén en la misma red WiFi

### **Error: "CLEARTEXT communication not permitted"**

**Causa:** Android bloquea HTTP por defecto

**Solución:** Ya está arreglado en `AndroidManifest.xml`:
```xml
android:usesCleartextTraffic="true"
```

### **Error: "El usuario ya existe"**

**Solución:** Usa otro username o limpia la base de datos:
```sql
USE hanashinomori;
DELETE FROM user_roles WHERE user_id > 13;
DELETE FROM users WHERE id > 13;
```

### **Backend no inicia**

**Solución:**
```powershell
# Detén procesos en puerto 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Reinicia backend
cd "c:\Users\darks\Documents\Proyectos Duoc\Aplicacione Moviles\HanashiNoMori-VSC"
.\mvnw.cmd clean spring-boot:run
```

---

## 📁 **ARCHIVOS CREADOS/MODIFICADOS**

### **Backend (Creados):**
- ✅ `SimpleRegisterRequest.java`
- ✅ `SimpleRegisterResponse.java`
- ✅ `SimpleLoginRequest.java`
- ✅ `SimpleLoginResponse.java`
- ✅ `AndroidAuthController.java`

### **Android (Modificados):**
- ✅ `RetrofitProvider.kt` (Solo cambio de URL)
- ✅ `AndroidManifest.xml` (Agregado `usesCleartextTraffic`)

---

## ✨ **VENTAJAS DE ESTE ENFOQUE**

1. ✅ **No tocas el código Android existente** (solo 2 líneas cambiadas)
2. ✅ **Backend adaptado a tu app** (no al revés)
3. ✅ **MVP funcional** (sin complejidad innecesaria)
4. ✅ **Fácil de debuggear** (logs claros en ambos lados)
5. ✅ **Perfecto para universidad** (simple y funcional)
6. ✅ **Mantiene los endpoints antiguos** (`/api/auth/*` siguen funcionando si los necesitas)

---

## 🎓 **PARA TU PROYECTO UNIVERSITARIO**

**Endpoints funcionales:**

1. **Registro:** `POST /users`
   ```json
   Request: { "username": "...", "email": "...", "password": "..." }
   Response: { "success": true, "message": "...", "userId": 1 }
   ```

2. **Login:** `POST /users/login`
   ```json
   Request: { "username": "...", "password": "..." }
   Response: { "success": true, "message": "...", "userId": 1, "username": "..." }
   ```

3. **Ping/Test:** `GET /users/ping`
   ```
   Response: "Backend Android MVP funcionando correctamente ✅"
   ```

---

## 📝 **CHECKLIST FINAL**

- [ ] Backend corriendo en puerto 8080
- [ ] Test de ping exitoso: `http://localhost:8080/users/ping`
- [ ] Test de registro exitoso (PowerShell)
- [ ] Test de login exitoso (PowerShell)
- [ ] RetrofitProvider.kt con URL correcta (10.0.2.2 o tu IP)
- [ ] AndroidManifest con `usesCleartextTraffic="true"`
- [ ] Android Studio: Build → Rebuild Project
- [ ] App ejecutándose en emulador/dispositivo
- [ ] Registro en app Android exitoso
- [ ] Login en app Android exitoso
- [ ] Logs visibles en Logcat (OkHttp)

---

## 🚀 **PRÓXIMOS PASOS (OPCIONAL)**

Una vez que funcione el login/registro:

1. **Guardar userId en SharedPreferences** (Android)
2. **Auto-login si existe userId guardado**
3. **Agregar endpoint para obtener datos de usuario:** `GET /users/{id}`
4. **Implementar CRUD de medios** (mangas/libros)
5. **Conectar biblioteca personal**

---

**¿TODO LISTO? ¡Prueba tu app! 🎉**

Si tienes algún error, revisa los logs en:
- **Backend:** Terminal de VSCode donde corre `spring-boot:run`
- **Android:** Logcat en Android Studio (filtro: `OkHttp`)
