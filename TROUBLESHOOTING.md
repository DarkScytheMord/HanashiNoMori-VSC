# 🔍 Troubleshooting: Error de Login

## ❌ Problema Actual

```json
{
    "success": false,
    "message": "Credenciales inválidas",
    "data": null
}
```

---

## 🔎 Diagnóstico paso a paso

### **Paso 1: Verificar que el usuario existe**

Ejecuta este endpoint de debug (después de reiniciar la app):

```
GET http://localhost:8080/api/debug/check-user/darkscythe?password=Password123!
```

**Esto te dirá:**
- ✅ Si el usuario existe en la BD
- ✅ Si el password coincide
- ✅ Si la cuenta está activa
- ✅ Qué roles tiene

---

### **Paso 2: Posibles causas del error**

#### **Causa 1: Usuario no encontrado**
El usuario no se guardó correctamente en la BD.

**Solución:**
- Verifica en los logs de la aplicación si hay algún error
- Revisa la terminal donde corre Spring Boot

#### **Causa 2: Password no coincide**
El password almacenado no es el que estás usando.

**Solución:**
- Usa el endpoint de debug para verificar
- El password debe estar encriptado con BCrypt en la BD

#### **Causa 3: Usuario inactivo**
El campo `is_active` está en FALSE.

**Solución:**
- Verifica con el endpoint de debug
- Por defecto debería ser TRUE

#### **Causa 4: Problema con los roles**
El usuario no tiene roles asignados correctamente.

**Solución:**
- Verifica con el endpoint de debug
- Debe tener al menos el rol "USER"

---

### **Paso 3: Revisar logs de la aplicación**

En la terminal donde corre Spring Boot, busca líneas como:

**Si el login falla, verás:**
```
ERROR c.H.H.Controller.AuthController : Error en login: Bad credentials
```

**Si hay un problema con la BD:**
```
ERROR o.h.engine.jdbc.spi.SqlExceptionHelper : ...
```

**Si el usuario no existe:**
```
WARN  o.s.s.c.bcrypt.BCryptPasswordEncoder : Empty encoded password
```

---

### **Paso 4: Prueba manual del password**

Si el endpoint de debug te dice que `passwordMatches: false`, significa que:

1. El password en la BD es diferente al que estás probando
2. Hubo un error al encriptar durante el registro

**Para solucionarlo**, crea un nuevo usuario con un endpoint de prueba:

```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "test123",
  "fullName": "Test User"
}
```

Y luego intenta hacer login con ese:

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "test123"
}
```

---

## 🔧 Solución Rápida

### **Opción A: Usar el endpoint de debug**

1. **Reinicia la aplicación** (para cargar el nuevo DebugController)

2. **Ejecuta en Postman:**
   ```
   GET http://localhost:8080/api/debug/check-user/darkscythe?password=Password123!
   ```

3. **Analiza la respuesta:**
   ```json
   {
       "success": true,
       "message": "Usuario verificado",
       "data": {
           "userExists": true,
           "userId": 3,
           "username": "darkscythe",
           "email": "dark@example.com",
           "isActive": true,
           "passwordInDb": "$2a$10$abcd1234...",
           "passwordMatches": true/false,  <-- IMPORTANTE
           "roles": ["USER"]
       }
   }
   ```

4. **Si `passwordMatches: false`:**
   - El password en la BD NO coincide con el que estás probando
   - Crea un nuevo usuario con un password simple como "test123"

5. **Si `passwordMatches: true`:**
   - El problema está en el `AuthenticationManager`
   - Revisa los logs de la aplicación

---

### **Opción B: Registrar nuevo usuario**

Para descartar problemas, registra un nuevo usuario:

```json
POST http://localhost:8080/api/auth/register

{
  "username": "usuario1",
  "email": "usuario1@example.com",
  "password": "pass123",
  "fullName": "Usuario Uno"
}
```

Y luego haz login:

```json
POST http://localhost:8080/api/auth/login

{
  "username": "usuario1",
  "password": "pass123"
}
```

---

## 📊 Checklist de Verificación

Marca cada uno cuando lo verifiques:

- [ ] La aplicación está corriendo sin errores
- [ ] El usuario "darkscythe" existe en la BD
- [ ] El password está encriptado en la BD (empieza con `$2a$10$`)
- [ ] El campo `is_active` = TRUE
- [ ] El usuario tiene rol "USER" asignado
- [ ] El endpoint de debug devuelve `passwordMatches: true`
- [ ] Los logs no muestran errores de SQL
- [ ] La tabla `user_roles` tiene un registro para el usuario

---

## 🆘 Si nada funciona

Si después de todo esto el login sigue fallando, comparte:

1. **Respuesta del endpoint de debug**
2. **Logs de la terminal donde corre Spring Boot** (últimas 20 líneas)
3. **Resultado de este query SQL:**
   ```sql
   SELECT u.id, u.username, u.email, u.is_active, 
          LEFT(u.password, 20) as password_start,
          r.name as role
   FROM users u
   LEFT JOIN user_roles ur ON u.id = ur.user_id
   LEFT JOIN roles r ON ur.role_id = r.id
   WHERE u.username = 'darkscythe';
   ```

---

**¡Empecemos con el Paso 1! Reinicia la app y prueba el endpoint de debug.** 🔍
