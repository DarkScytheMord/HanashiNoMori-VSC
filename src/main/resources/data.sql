-- ============================================
-- HanashiNoMori - Datos Iniciales
-- Se carga automaticamente al iniciar el backend
-- ============================================

-- ============================================
-- MANGA (10 libros)
-- ============================================

INSERT IGNORE INTO books (id, title, author, category, description, cover_url, isbn, created_at, updated_at) VALUES
(1, 'Naruto', 'Masashi Kishimoto', 'Manga', 'Naruto Uzumaki es un joven ninja que busca reconocimiento de sus companeros y suena con convertirse en el Hokage, el lider de su aldea. La historia sigue sus aventuras mientras entrena para convertirse en el ninja mas fuerte y proteger a sus seres queridos.', 'https://m.media-amazon.com/images/I/81KLvXuB+rL._AC_UF894,1000_QL80_.jpg', '9781421500720', NOW(), NOW()),
(2, 'One Piece', 'Eiichiro Oda', 'Manga', 'Monkey D. Luffy y su tripulacion pirata navegan por el Grand Line en busca del legendario tesoro One Piece para que Luffy se convierta en el Rey de los Piratas. En su camino, enfrentan enemigos poderosos, hacen nuevos amigos y descubren los secretos del mundo.', 'https://m.media-amazon.com/images/I/81QPHThrE8L._AC_UF894,1000_QL80_.jpg', '9781421536255', NOW(), NOW()),
(3, 'Attack on Titan', 'Hajime Isayama', 'Manga', 'La humanidad vive dentro de enormes murallas para protegerse de los titanes, gigantes humanoides que devoran personas sin razon aparente. Eren Yeager jura venganza despues de que un titan destruye su ciudad natal y devora a su madre.', 'https://m.media-amazon.com/images/I/81OSiWKV88L._AC_UF894,1000_QL80_.jpg', '9781612620244', NOW(), NOW()),
(4, 'Death Note', 'Tsugumi Ohba', 'Manga', 'Light Yagami encuentra un cuaderno sobrenatural que le permite matar a cualquiera cuyo nombre escriba en el. Decide usar el Death Note para crear un mundo sin criminales, pero pronto es perseguido por el brillante detective L.', 'https://m.media-amazon.com/images/I/71SjJH0a3iL._AC_UF894,1000_QL80_.jpg', '9781421501376', NOW(), NOW()),
(5, 'My Hero Academia', 'Kohei Horikoshi', 'Manga', 'En un mundo donde el 80% de la poblacion tiene superpoderes llamados "quirks", Izuku Midoriya nace sin ninguno. Sin embargo, su determinacion lo lleva a heredar el quirk del heroe numero uno y cumplir su sueno de convertirse en un gran heroe.', 'https://m.media-amazon.com/images/I/81gPcoGsCSL._AC_UF894,1000_QL80_.jpg', '9781421582696', NOW(), NOW()),
(6, 'Demon Slayer', 'Koyoharu Gotouge', 'Manga', 'Tanjiro Kamado se convierte en un cazador de demonios despues de que su familia es asesinada y su hermana Nezuko es convertida en demonio. Busca una cura mientras lucha contra demonios poderosos y protege a los inocentes.', 'https://m.media-amazon.com/images/I/81tW1-sKGsL._AC_UF894,1000_QL80_.jpg', '9781974700523', NOW(), NOW()),
(7, 'Tokyo Ghoul', 'Sui Ishida', 'Manga', 'Ken Kaneki es un estudiante timido que se convierte en un hibrido mitad humano, mitad ghoul despues de un encuentro mortal. Debe aprender a vivir entre ambos mundos mientras lucha por mantener su humanidad.', 'https://m.media-amazon.com/images/I/81gMPrZpXbL._AC_UF894,1000_QL80_.jpg', '9781421580364', NOW(), NOW()),
(8, 'Fullmetal Alchemist', 'Hiromu Arakawa', 'Manga', 'Los hermanos Edward y Alphonse Elric intentan usar la alquimia para resucitar a su madre, pero el experimento sale mal. Ahora buscan la Piedra Filosofal para recuperar lo que perdieron.', 'https://m.media-amazon.com/images/I/91R+5zGt9QL._AC_UF894,1000_QL80_.jpg', '9781591169208', NOW(), NOW()),
(9, 'Bleach', 'Tite Kubo', 'Manga', 'Ichigo Kurosaki obtiene accidentalmente los poderes de un Shinigami y debe asumir los deberes de defender a los humanos de los espiritus malignos y guiar las almas al mas alla.', 'https://m.media-amazon.com/images/I/81tTFwN6JSL._AC_UF894,1000_QL80_.jpg', '9781421506197', NOW(), NOW()),
(10, 'Jujutsu Kaisen', 'Gege Akutami', 'Manga', 'Yuji Itadori se une a una escuela de hechiceria despues de tragar un dedo maldito. Ahora alberga al Rey de las Maldiciones en su cuerpo y debe aprender a controlarlo mientras lucha contra espiritus malignos.', 'https://m.media-amazon.com/images/I/81PGR+t7RoL._AC_UF894,1000_QL80_.jpg', '9781974710027', NOW(), NOW());

-- ============================================
-- MANHWA (10 libros)
-- ============================================

INSERT IGNORE INTO books (id, title, author, category, description, cover_url, isbn, created_at, updated_at) VALUES
(11, 'Solo Leveling', 'Chugong', 'Manhwa', 'Sung Jin-Woo es el cazador de rango E mas debil de la humanidad. Despues de sobrevivir a una mazmorra mortal, obtiene el poder de subir de nivel sin limites y se embarca en un viaje para convertirse en el cazador mas fuerte.', 'https://m.media-amazon.com/images/I/81FKLMboZQL._AC_UF894,1000_QL80_.jpg', '9791138244014', NOW(), NOW()),
(12, 'Tower of God', 'SIU', 'Manhwa', 'Bam entra a la Torre, una estructura misteriosa donde aquellos que llegan a la cima pueden tener cualquier cosa que deseen. Su objetivo es encontrar a Rachel, la unica persona importante en su vida.', 'https://m.media-amazon.com/images/I/71+PbH5YLIL._AC_UF894,1000_QL80_.jpg', '9791138341653', NOW(), NOW()),
(13, 'The Beginning After The End', 'TurtleMe', 'Manhwa', 'El rey Grey posee una fuerza sin igual. Tras reencarnar en un nuevo mundo lleno de magia, le es dada una segunda oportunidad para corregir sus errores del pasado.', 'https://m.media-amazon.com/images/I/81Xd7D+iFoL._AC_UF894,1000_QL80_.jpg', '9791138341660', NOW(), NOW()),
(14, 'Omniscient Readers Viewpoint', 'Sing Shong', 'Manhwa', 'Kim Dokja es el unico lector que ha terminado una novela web de 3,149 capitulos. Cuando el mundo se convierte en ese libro, el es el unico que conoce el futuro y debe usarlo para sobrevivir.', 'https://m.media-amazon.com/images/I/81kO5RQJQTL._AC_UF894,1000_QL80_.jpg', '9791138341677', NOW(), NOW()),
(15, 'Noblesse', 'Son Jeho', 'Manhwa', 'Cadis Etrama Di Raizel, un noble vampiro, despierta despues de 820 anos de sueno. Se encuentra en la era moderna y debe adaptarse mientras protege a sus nuevos amigos humanos.', 'https://m.media-amazon.com/images/I/81Y8aJ0MJHL._AC_UF894,1000_QL80_.jpg', '9791138341684', NOW(), NOW()),
(16, 'The God of High School', 'Yongje Park', 'Manhwa', 'Un torneo epico invita a los mejores luchadores de secundaria. Los ganadores obtienen lo que deseen, pero hay un misterio oculto que involucra dioses y poderes sobrenaturales.', 'https://m.media-amazon.com/images/I/81uZ8ZYJ9KL._AC_UF894,1000_QL80_.jpg', '9791138341691', NOW(), NOW()),
(17, 'Eleceed', 'Son Jeho', 'Manhwa', 'Jiwoo es un joven amable con reflejos increibles y la habilidad de controlar la electricidad. Su vida cambia cuando salva a un gato que resulta ser un poderoso despertador.', 'https://m.media-amazon.com/images/I/81V0pHl2UJL._AC_UF894,1000_QL80_.jpg', '9791138341707', NOW(), NOW()),
(18, 'The Breaker', 'Jeon Geuk-jin', 'Manhwa', 'Shi-Woon Yi es un estudiante timido constantemente intimidado. Su vida cambia cuando su nuevo maestro resulta ser un maestro de artes marciales y le suplica que lo entrene.', 'https://m.media-amazon.com/images/I/81ZqJYvH+8L._AC_UF894,1000_QL80_.jpg', '9791138341714', NOW(), NOW()),
(19, 'Hardcore Leveling Warrior', 'Sehoon Kim', 'Manhwa', 'En el juego Lucid Adventure, Hardcore Leveling Warrior es el jugador numero 1. Tras ser traicionado y perder todo, debe subir de nivel desde cero mientras busca venganza.', 'https://m.media-amazon.com/images/I/81PXGjYdRZL._AC_UF894,1000_QL80_.jpg', '9791138341721', NOW(), NOW()),
(20, 'Sweet Home', 'Carnby Kim', 'Manhwa', 'Despues de una tragedia familiar, Hyun Cha se muda a un nuevo apartamento. Cuando los humanos comienzan a convertirse en monstruos, debe sobrevivir en este nuevo mundo aterrador.', 'https://m.media-amazon.com/images/I/81V4fF7VYXL._AC_UF894,1000_QL80_.jpg', '9791138341738', NOW(), NOW());

-- ============================================
-- DONGHUA (10 libros)
-- ============================================

INSERT IGNORE INTO books (id, title, author, category, description, cover_url, isbn, created_at, updated_at) VALUES
(21, 'Mo Dao Zu Shi', 'Mo Xiang Tong Xiu', 'Donghua', 'Wei Wuxian, el fundador de la cultivacion demoniaca, resucita 13 anos despues de su muerte. Se reune con Lan Wangji para resolver misterios sobrenaturales mientras descubren la verdad sobre su pasado.', 'https://m.media-amazon.com/images/I/71VuCvj3QoL._AC_UF894,1000_QL80_.jpg', '9787559456458', NOW(), NOW()),
(22, 'Heaven Officials Blessing', 'Mo Xiang Tong Xiu', 'Donghua', 'Xie Lian, un principe que ascendio a la divinidad, es desterrado del cielo por tercera vez. Mientras investiga desapariciones misteriosas, conoce a un joven con un secreto increible.', 'https://m.media-amazon.com/images/I/81PXGjYdRZL._AC_UF894,1000_QL80_.jpg', '9787559468734', NOW(), NOW()),
(23, 'The Kings Avatar', 'Butterfly Blue', 'Donghua', 'Ye Xiu, un campeon de esports, es forzado a retirarse. Sin embargo, comienza desde cero en un nuevo servidor con el objetivo de regresar a la cima de la gloria profesional.', 'https://m.media-amazon.com/images/I/71nU2CgQ3mL._AC_UF894,1000_QL80_.jpg', '9787559459824', NOW(), NOW()),
(24, 'Scissor Seven', 'He Xiaofeng', 'Donghua', 'Seven es un peluquero amnesico que trabaja como asesino a sueldo, pero es terrible en su trabajo. Mientras busca recuperar sus memorias, se involucra en situaciones comicas y peligrosas.', 'https://m.media-amazon.com/images/I/81V4fF7VYXL._AC_UF894,1000_QL80_.jpg', '9787559462341', NOW(), NOW()),
(25, 'Link Click', 'Li Haoling', 'Donghua', 'Cheng Xiaoshi y Lu Guang dirigen un estudio fotografico con un secreto: pueden viajar al pasado a traves de fotografias. Pronto descubren que cambiar el pasado tiene consecuencias.', 'https://m.media-amazon.com/images/I/81qVw5DqLvL._AC_UF894,1000_QL80_.jpg', '9787559463852', NOW(), NOW()),
(26, 'Hitori no Shita', 'Dong Man Tang', 'Donghua', 'Zhang Chulan es un joven aparentemente ordinario hasta que es atacado por zombis. Descubre que pertenece a una sociedad secreta de personas con habilidades especiales.', 'https://m.media-amazon.com/images/I/81Z1bJ7PZRL._AC_UF894,1000_QL80_.jpg', '9787559465791', NOW(), NOW()),
(27, 'Soul Land', 'Tang Jia San Shao', 'Donghua', 'Tang San es un maestro de armas ocultas. Despues de su muerte, renace en un mundo de cultivacion marcial donde debe adaptarse y volverse mas fuerte para proteger a sus seres queridos.', 'https://m.media-amazon.com/images/I/81iYs6qJVzL._AC_UF894,1000_QL80_.jpg', '9787559467234', NOW(), NOW()),
(28, 'Battle Through the Heavens', 'Tian Can Tu Dou', 'Donghua', 'Xiao Yan era un genio de la cultivacion, pero perdio todos sus poderes. Descubre que un espiritu en su anillo robo su poder y ahora debe entrenar desde cero para recuperar su fuerza.', 'https://m.media-amazon.com/images/I/81Y8aJ0MJHL._AC_UF894,1000_QL80_.jpg', '9787559468123', NOW(), NOW()),
(29, 'Fog Hill of Five Elements', 'Lin Hun', 'Donghua', 'En un mundo donde los cinco elementos son la base de todo poder, guerreros deben proteger su tierra de invasores. Una experiencia visual espectacular con historia profunda sobre lealtad.', 'https://m.media-amazon.com/images/I/81V0pHl2UJL._AC_UF894,1000_QL80_.jpg', '9787559469567', NOW(), NOW()),
(30, 'The Daily Life of the Immortal King', 'Kuxuan', 'Donghua', 'Wang Ling es un estudiante extraordinariamente poderoso que solo quiere vivir tranquilo. Constantemente se ve envuelto en situaciones peligrosas donde debe usar sus poderes sin revelar su fuerza.', 'https://m.media-amazon.com/images/I/81PXGjYdRZL._AC_UF894,1000_QL80_.jpg', '9787559470234', NOW(), NOW());

-- ============================================
-- Mensaje de confirmacion
-- ============================================
-- Los 30 libros se han cargado correctamente con IDs unicos (1-30):
-- - 10 Manga (IDs 1-10)
-- - 10 Manhwa (IDs 11-20)
-- - 10 Donghua (IDs 21-30)
-- Nota: INSERT IGNORE evita duplicados al reiniciar el backend
