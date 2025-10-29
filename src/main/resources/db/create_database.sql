DROP TABLE IF EXISTS acompanantes;
DROP TABLE IF EXISTS hijos;
DROP TABLE IF EXISTS inscripcion;
DROP TABLE IF EXISTS reserva;
DROP TABLE IF EXISTS alquiler;
DROP TABLE IF EXISTS embarcacion;
DROP TABLE IF EXISTS patron;
DROP TABLE IF EXISTS socios;

CREATE TABLE IF NOT EXISTS `socios` (
    `dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `nombre` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `apellidos` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `fecha_nacimiento` DATE DEFAULT NULL,
    `direccion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `fecha_inscripcion` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `es_titular` BOOLEAN NOT NULL DEFAULT FALSE,
    `titulo_patron` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `patron` (
    `nombre` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `apellidos` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
    `fecha_nacimiento` DATE NOT NULL,
    `fecha_expedicion_titulo` DATE NOT NULL,
    PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `embarcacion` (
    `matricula` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
    `nombre` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL UNIQUE,
    `tipo` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `plazas` INT(11) NOT NULL,
    `dimensiones` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `id_patron` VARCHAR(255) COLLATE utf8_unicode_ci ,
    PRIMARY KEY (`matricula`),
    FOREIGN KEY (`id_patron`) REFERENCES patron(`dni`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `alquiler` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `fechainicio` DATE NOT NULL,
    `fechafin` DATE NOT NULL,
    `precio` DOUBLE NOT NULL,
    `plazas` INT(11) NOT NULL,
    `usuario_dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `matricula_embarcacion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`usuario_dni`) REFERENCES socios(`dni`),
    FOREIGN KEY (`matricula_embarcacion`) REFERENCES embarcacion(`matricula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `reserva` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `fecha` DATE NOT NULL,
    `plazas` INT(11) NOT NULL,
    `precio` DOUBLE NOT NULL,
    `usuario_id` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `matricula_embarcacion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `descripcion` TEXT COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`usuario_id`) REFERENCES socios(`dni`),
    FOREIGN KEY (`matricula_embarcacion`) REFERENCES embarcacion(`matricula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `inscripcion` (
                                             `id` INT(11) NOT NULL AUTO_INCREMENT,
    `socio_Titular` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `cuota_anual` DOUBLE NOT NULL,
    `fecha_creacion` DATE NOT NULL,
    `segundo_adulto` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`socio_Titular`) REFERENCES socios(`dni`),
    FOREIGN KEY (`segundo_adulto`) REFERENCES socios(`dni`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `hijos` (
    `dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `id_inscripcion` INT(11) NOT NULL,
    PRIMARY KEY (`dni`),
    FOREIGN KEY (`dni`) REFERENCES socios(`dni`),
    FOREIGN KEY (`id_inscripcion`) REFERENCES inscripcion(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `acompanantes` (
    `dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
    `id_alquiler` INT(11) NOT NULL,
    PRIMARY KEY (`dni`),
    FOREIGN KEY (`id_alquiler`) REFERENCES alquiler(`id`),
    FOREIGN KEY (`dni`) REFERENCES socios(`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


