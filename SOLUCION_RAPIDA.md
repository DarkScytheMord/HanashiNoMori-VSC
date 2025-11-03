# 🔧 Solución Inmediata: Arreglar Usuario en MySQL

## 🎯 Problema
El usuario `darkscythe` no tiene roles asignados, por eso el login falla.

---

## ✅ Solución (2 opciones)

### **OPCIÓN 1: Usar HeidiSQL, MySQL Workbench o similar**

1. **Abre tu cliente de MySQL** (HeidiSQL, MySQL Workbench, phpMyAdmin, etc.)

2. **Selecciona la base de datos:**
   ```sql
   USE hanashinomori;
   ```

3. **Ejecuta estos 3 comandos:**

   ```sql
   -- 1. Asegurarse de que existe el rol USER
   INSERT INTO roles (name, description) 
   VALUES ('USER', 'Usuario estándar')
   ON DUPLICATE KEY UPDATE description = 'Usuario estándar';
   
   -- 2. Eliminar asignaciones previas del usuario 3 (por si acaso)
   DELETE FROM user_roles WHERE user_id = 3;
   
   -- 3. Asignar el rol USER al usuario darkscythe
   INSERT INTO user_roles (user_id, role_id)
   SELECT 3, r.id FROM roles r WHERE r.name = 'USER';
   ```

4. **Verificar que funcionó:**
   ```sql
   SELECT u.username, r.name as rol
   FROM users u
   JOIN user_roles ur ON u.id = ur.user_id
   JOIN roles r ON ur.role_id = r.id
   WHERE u.username = 'darkscythe';
   ```

   **Deberías ver:**
   ```
   username    | rol
   ------------|------
   darkscythe  | USER
   ```

---

### **OPCIÓN 2: Usar el script SQL que preparé**

1. **Abre el archivo:**
   ```
   database/FIX_USER_DARKSCYTHE.sql
   ```

2. **Copia todo el contenido**

3. **Pégalo en tu cliente MySQL y ejecuta**

---

## 🧪 Verificar que funcionó

Una vez ejecutado el script SQL, **SIN REINICIAR LA APP**, prueba en Postman:

```
GET http://localhost:8080/api/debug/check-user/darkscythe?password=Password123!
```

**Deberías ver:**
```json
{
    "success": true,
    "message": "Usuario verificado",
    "data": {
        "rolesCount": 1,           ← DEBE SER 1 AHORA
        "roles": ["USER"],         ← DEBE TENER "USER"
        "passwordMatches": true
    }
}
```

---

## 🚀 Probar el Login

Si el endpoint anterior muestra `rolesCount: 1`, entonces prueba el login:

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "darkscythe",
  "password": "Password123!"
}
```

**Resultado esperado:**
```json
{
    "success": true,
    "message": "Login exitoso",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "username": "darkscythe",
        "roles": ["USER"]
    }
}
```

---

## 🔍 Si aún no funciona

Si después de ejecutar el SQL el login sigue fallando:

1. **Reinicia la aplicación Spring Boot**
   - Detén la app (Ctrl+C)
   - Vuelve a ejecutar: `.\mvnw.cmd spring-boot:run`

2. **Verifica en MySQL que el rol está asignado:**
   ```sql
   SELECT * FROM user_roles WHERE user_id = 3;
   ```
   Debe retornar al menos 1 fila.

3. **Si ves una fila pero el login falla**, comparte:
   - Los logs de la aplicación (últimas 20 líneas)
   - La respuesta del endpoint de debug

---

## 📝 Resumen Visual

```
1. Ejecutar SQL
   ↓
2. Verificar con: GET /api/debug/check-user/darkscythe?password=...
   ↓
   ¿rolesCount: 1?
   ↓
   SÍ → Probar login
   NO → Reiniciar app y verificar de nuevo
```

---

## 🎯 **¿Qué hacer ahora?**

1. **Ejecuta el SQL** (OPCIÓN 1 o 2)
2. **Comparte el resultado** del endpoint debug
3. Si funciona, **prueba el login**

**¿Ya ejecutaste el SQL? ¿Qué resultado obtuviste?** 🔍
