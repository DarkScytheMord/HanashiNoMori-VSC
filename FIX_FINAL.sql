-- ================================================================
-- SOLUCIÓN FINAL: Asignar rol USER al usuario darkscythe
-- ================================================================
USE hanashinomori;

-- Paso 1: Verificar estado ANTES
SELECT '=== ANTES DEL FIX ===' as status;
SELECT 
    u.id, 
    u.username, 
    u.email, 
    COUNT(ur.role_id) as roles_count
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
WHERE u.username = 'darkscythe'
GROUP BY u.id, u.username, u.email;

-- Paso 2: Asegurar que existe el rol USER
INSERT INTO roles (name, description) 
VALUES ('USER', 'Usuario estándar')
ON DUPLICATE KEY UPDATE description = 'Usuario estándar';

-- Paso 3: Limpiar cualquier asignación previa (por si acaso)
DELETE FROM user_roles WHERE user_id = 3;

-- Paso 4: Asignar rol USER al usuario darkscythe
INSERT INTO user_roles (user_id, role_id)
SELECT 3, r.id 
FROM roles r 
WHERE r.name = 'USER' 
LIMIT 1;

-- Paso 5: Verificar que funcionó
SELECT '=== DESPUÉS DEL FIX ===' as status;
SELECT 
    u.id, 
    u.username, 
    u.email, 
    r.name as rol,
    ur.user_id,
    ur.role_id
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.username = 'darkscythe';

-- FIN DEL SCRIPT
-- Ahora puedes hacer login con darkscythe
