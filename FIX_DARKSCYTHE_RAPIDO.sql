-- ========================================
-- ARREGLO RÁPIDO PARA USUARIO DARKSCYTHE
-- ========================================
-- Ejecutar en MySQL para asignar rol USER

USE hanashinomori;

-- 1. Ver el estado actual
SELECT 'ANTES DEL FIX:' as status;
SELECT u.id, u.username, u.email, COUNT(ur.role_id) as roles_count
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
WHERE u.username = 'darkscythe'
GROUP BY u.id;

-- 2. Asegurar que existe el rol USER
INSERT INTO roles (name, description) 
VALUES ('USER', 'Usuario estándar')
ON DUPLICATE KEY UPDATE description = 'Usuario estándar';

-- 3. Eliminar asignaciones previas (por si acaso)
DELETE FROM user_roles WHERE user_id = 3;

-- 4. Asignar rol USER al usuario darkscythe (id=3)
INSERT INTO user_roles (user_id, role_id)
SELECT 3, r.id FROM roles r WHERE r.name = 'USER' LIMIT 1;

-- 5. Verificar que funcionó
SELECT 'DESPUÉS DEL FIX:' as status;
SELECT u.id, u.username, u.email, r.name as rol
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.username = 'darkscythe';

-- FIN DEL SCRIPT
