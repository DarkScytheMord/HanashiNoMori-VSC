-- ================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- HanashiNoMori Backend
-- ================================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS hanashinomori CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hanashinomori;

-- ================================================
-- TABLA: users
-- ================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    avatar_url VARCHAR(500),
    bio TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: roles
-- ================================================
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar roles iniciales
INSERT INTO roles (name, description) VALUES 
    ('USER', 'Usuario estándar'),
    ('ADMIN', 'Administrador del sistema'),
    ('MODERATOR', 'Moderador de contenido')
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- ================================================
-- TABLA: user_roles (Relación muchos a muchos)
-- ================================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: categories
-- ================================================
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    icon VARCHAR(50),
    description VARCHAR(255),
    color_hex VARCHAR(7),
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar categorías iniciales
INSERT INTO categories (name, icon, color_hex, description, display_order) VALUES 
    ('Libro', '📚', '#4CAF50', 'Libros y novelas', 1),
    ('Manga', '🎌', '#FF5722', 'Manga japonés', 2),
    ('Manhwa', '🇰🇷', '#2196F3', 'Manhwa coreano', 3),
    ('Donghua', '🇨🇳', '#FFC107', 'Donghua chino', 4)
ON DUPLICATE KEY UPDATE 
    icon=VALUES(icon), 
    color_hex=VALUES(color_hex),
    description=VALUES(description),
    display_order=VALUES(display_order);

-- ================================================
-- TABLA: media
-- ================================================
CREATE TABLE IF NOT EXISTS media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category_id BIGINT NOT NULL,
    description TEXT,
    cover_url VARCHAR(500),
    publication_year INT,
    total_chapters INT,
    status VARCHAR(50) DEFAULT 'EN_CURSO',
    is_custom BOOLEAN DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_category (category_id),
    INDEX idx_title (title),
    INDEX idx_author (author)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: tags
-- ================================================
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    color_hex VARCHAR(7) DEFAULT '#808080',
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar tags iniciales
INSERT INTO tags (name, color_hex, category) VALUES 
    ('Acción', '#FF5722', 'Género'),
    ('Aventura', '#4CAF50', 'Género'),
    ('Romance', '#E91E63', 'Género'),
    ('Comedia', '#FFC107', 'Género'),
    ('Drama', '#9C27B0', 'Género'),
    ('Fantasía', '#3F51B5', 'Género'),
    ('Sci-Fi', '#00BCD4', 'Género'),
    ('Terror', '#212121', 'Género'),
    ('Misterio', '#795548', 'Género'),
    ('Histórico', '#FF9800', 'Género')
ON DUPLICATE KEY UPDATE 
    color_hex=VALUES(color_hex),
    category=VALUES(category);

-- ================================================
-- TABLA: media_tags (Relación muchos a muchos)
-- ================================================
CREATE TABLE IF NOT EXISTS media_tags (
    media_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (media_id, tag_id),
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: user_library
-- ================================================
CREATE TABLE IF NOT EXISTS user_library (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    is_favorite BOOLEAN DEFAULT FALSE,
    rating INT,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_read_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_media (user_id, media_id),
    INDEX idx_user (user_id),
    INDEX idx_favorite (is_favorite)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: reading_progress
-- ================================================
CREATE TABLE IF NOT EXISTS reading_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    current_chapter INT DEFAULT 0,
    current_page INT DEFAULT 0,
    percentage_completed DECIMAL(5,2) DEFAULT 0.00,
    time_spent_minutes INT DEFAULT 0,
    last_read_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_media_progress (user_id, media_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: reviews
-- ================================================
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    rating INT NOT NULL,
    title VARCHAR(200),
    comment TEXT,
    likes_count INT DEFAULT 0,
    is_spoiler BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    INDEX idx_media_reviews (media_id),
    INDEX idx_user_reviews (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: collections
-- ================================================
CREATE TABLE IF NOT EXISTS collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    cover_url VARCHAR(500),
    items_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_collections (user_id),
    INDEX idx_public_collections (is_public)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: collection_media (Relación muchos a muchos)
-- ================================================
CREATE TABLE IF NOT EXISTS collection_media (
    collection_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    display_order INT DEFAULT 0,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (collection_id, media_id),
    FOREIGN KEY (collection_id) REFERENCES collections(id) ON DELETE CASCADE,
    FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: notifications
-- ================================================
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    related_entity_type VARCHAR(50),
    related_entity_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    action_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_notifications (user_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: activity_log
-- ================================================
CREATE TABLE IF NOT EXISTS activity_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    description VARCHAR(500),
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_activity (user_id),
    INDEX idx_action_type (action_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- ACTUALIZACIÓN PARA ANDROID APP
-- ================================================

-- Renombrar tablas existentes
RENAME TABLE media TO books;
RENAME TABLE user_library TO favorites;

-- Agregar constraint único en favorites
ALTER TABLE favorites 
ADD UNIQUE KEY unique_user_book (user_id, book_id);

-- ================================================
-- TABLA: authors
-- ================================================
CREATE TABLE IF NOT EXISTS authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    bio TEXT,
    photo_url VARCHAR(500),
    country VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: publishers
-- ================================================
CREATE TABLE IF NOT EXISTS publishers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    website VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: book_authors (Relación muchos a muchos)
-- ================================================
CREATE TABLE IF NOT EXISTS book_authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    role VARCHAR(50),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE,
    UNIQUE KEY unique_book_author (book_id, author_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: user_preferences
-- ================================================
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    theme VARCHAR(20) DEFAULT 'light',
    notifications_enabled BOOLEAN DEFAULT TRUE,
    favorite_category VARCHAR(50),
    language VARCHAR(10) DEFAULT 'es',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- DATOS DE PRUEBA: Libros
-- ================================================
INSERT INTO books (title, author, category, description) VALUES
('Naruto', 'Masashi Kishimoto', 'Manga', 'Historia de un ninja que busca ser Hokage'),
('One Piece', 'Eiichiro Oda', 'Manga', 'Piratas en busca del tesoro One Piece'),
('Attack on Titan', 'Hajime Isayama', 'Manga', 'Humanidad vs Titanes'),
('Death Note', 'Tsugumi Ohba', 'Manga', 'Cuaderno de la muerte'),
('Solo Leveling', 'Chugong', 'Manhwa', 'Cazador de monstruos que sube de nivel'),
('Tower of God', 'SIU', 'Manhwa', 'Escalando la torre misteriosa'),
('The Beginning After The End', 'TurtleMe', 'Manhwa', 'Rey reencarnado en un mundo mágico'),
('Link Click', 'Studio Lan', 'Donghua', 'Viajes en el tiempo a través de fotografías'),
('The King''s Avatar', 'Butterfly Blue', 'Donghua', 'Jugador profesional de eSports'),
('Mo Dao Zu Shi', 'Mo Xiang Tong Xiu', 'Donghua', 'Cultivador reencarnado');

-- ================================================
-- FIN DEL SCRIPT
-- ================================================
