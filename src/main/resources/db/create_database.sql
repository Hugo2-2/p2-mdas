

DROP TABLE IF EXISTS `socios`;
DROP TABLE IF EXISTS `alquiler`;
DROP TABLE IF EXISTS `reserva`;
DROP TABLE IF EXISTS `embarcacion`;
DROP TABLE IF EXISTS `patron`;

CREATE TABLE IF NOT EXISTS `socios` (
                                        `id` INT(11) NOT NULL,
    `nombre` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `apellidos` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `fecha_nacimiento` DATE DEFAULT NULL,
    `direccion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `fecha_inscripcion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `es_titular` BOOLEAN NOT NULL DEFAULT FALSE,
    `titulo_patron` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE IF NOT EXISTS `alquiler` (
    `id` INT(11) NOT NULL,
    `fechainicio` DATE NOT NULL,
    `fechafin` DATE NOT NULL,
    `precio` DOUBLE NOT NULL,
    `plazas` INT(11) NOT NULL,
    `usuario_dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `acompanantes_dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `matricula_embarcacion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


INSERT INTO `alquiler` VALUES
    (0, '2025-07-01', '2025-07-10', 1200.00, 4, '12345678A', '87654321B,11223344C', 'ABC1234'),
    (1, '2025-08-05', '2025-08-12', 900.50, 3, '26325825W', '33445566D', 'XYZ9876'),
    (2, '2025-09-15', '2025-09-20', 650.00, 2, '26325825W', '33445866D', 'LMN2468');



CREATE TABLE IF NOT EXISTS `reserva` (
   `id` INT(11) NOT NULL,
   `fecha` DATE NOT NULL,
   `plazas` INT(11) NOT NULL,
   `precio` DOUBLE NOT NULL,
   `usuario_id` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
   `matricula_embarcacion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
   `descripcion` TEXT COLLATE utf8_unicode_ci DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


INSERT INTO `reserva` VALUES
  (0, '2025-06-01', 4, 350.00, '12345678A', 'ABC1234', 'Reserva de embarcación para paseo familiar.'),
  (1, '2025-06-15', 2, 200.00, '26325825W', 'XYZ9876', 'Reserva para pesca deportiva.'),
  (2, '2025-07-20', 6, 500.00, '26325825W', 'LMN2468', 'Salida de grupo turístico.');

CREATE TABLE `embarcacion` (
    `matricula` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `nombre` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `tipo` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `plazas` INT(11) NOT NULL,
    `dimensiones` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `id_patron` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`matricula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

    
CREATE TABLE `patron` (
    `id` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `nombre` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `apellidos` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
    `fecha_nacimiento` DATE NOT NULL,
    `fecha_expedicion_titulo` DATE NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;