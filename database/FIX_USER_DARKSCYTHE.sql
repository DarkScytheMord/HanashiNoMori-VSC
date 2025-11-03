-- ================================================================
-- SCRIPT RÁPIDO: Asignar rol USER al usuario darkscythe
-- ================================================================

USE hanashinomori;

-- Ver el estado actual
SELECT 'ANTES DE LA CORRECCIÓN:' as estado;
SELECT u.id, u.username, u.email, 
       COALESCE(r.name, 'SIN ROL') as rol
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE u.username = 'darkscythe';

-- Asegurarse de que el rol USER existe
INSERT INTO roles (name, description) 
VALUES ('USER', 'Usuario estándar')
ON DUPLICATE KEY UPDATE description = 'Usuario estándar';

-- Asignar el rol USER al usuario darkscythe (id=3)
-- Primero eliminar cualquier asignación existente (por si acaso)
DELETE FROM user_roles WHERE user_id = 3;

-- Ahora insertar la nueva asignación
INSERT INTO user_roles (user_id, role_id)
SELECT 3, r.id
FROM roles r
WHERE r.name = 'USER'
LIMIT 1;

-- Ver el resultado
SELECT 'DESPUÉS DE LA CORRECCIÓN:' as estado;
SELECT u.id, u.username, u.email, r.name as rol, ur.assigned_at
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.username = 'darkscythe';

SELECT '✅ ROL ASIGNADO EXITOSAMENTE' as resultado;
