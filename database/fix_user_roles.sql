-- Script para arreglar el usuario darkscythe
-- Asignar el rol USER al usuario

USE hanashinomori;

-- Verificar que el rol USER existe
SELECT * FROM roles WHERE name = 'USER';

-- Si no existe, créalo
INSERT IGNORE INTO roles (name, description) VALUES ('USER', 'Usuario estándar');

-- Asignar el rol USER al usuario darkscythe (id = 3)
INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT 3, r.id
FROM roles r
WHERE r.name = 'USER';

-- Verificar que se asignó correctamente
SELECT 
    u.id,
    u.username,
    u.email,
    r.name as role,
    ur.assigned_at
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE u.username = 'darkscythe';
