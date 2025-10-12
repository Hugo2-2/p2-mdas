-- phpMyAdmin SQL Dump
-- version 2.7.0-pl2
-- http://www.phpmyadmin.net
-- 
-- Servidor: oraclepr.uco.es
-- Tiempo de generación: 12-10-2025 a las 12:06:17
-- Versión del servidor: 5.1.73
-- Versión de PHP: 5.3.3
-- 
-- Base de datos: `i32esrah`
-- 
CREATE DATABASE `i32esrah` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE i32esrah;

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `socios`
-- 

CREATE TABLE `socios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `apellidos` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `dni` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `fecha_inscripcion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `es_titular` tinyint(1) NOT NULL DEFAULT '0',
  `titulo_patron` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `socios`
-- 

INSERT INTO `socios` VALUES (0, 'Carlos', 'Pérez Gómez', '12345678A', '1990-05-12', 'Calle Sol 45, Madrid', '2025-10-11 15:28:45', 1, 1);
INSERT INTO `socios` VALUES (1, 'Jose Luis', 'Garcia Corbacho', '26325825W', '2005-06-22', 'Aira', '2025-10-11 00:00:00', 1, 1);
INSERT INTO `socios` VALUES (2, 'Hugo', 'Espejo Ramírez', '26325825W', '2005-07-22', 'Aira', '2025-10-11 00:00:00', 1, 1);

-- --------------------------------------------------------
-- 
-- Estructura de tabla para la tabla `alquiler`
-- 
CREATE TABLE `alquiler` (
  `id` INT(11) NOT NULL,
  `fechainicio` DATE NOT NULL,
  `fechafin` DATE NOT NULL,
  `precio` DOUBLE NOT NULL,
  `plazas` INT(11) NOT NULL,
  `usuario_dni` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `acompanantes_dni` TEXT COLLATE utf8_unicode_ci DEFAULT NULL,
  `matricula_embarcacion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `alquiler`
-- 
INSERT INTO `alquiler` VALUES 
(0, '2025-07-01', '2025-07-10', 1200.00, 4, '12345678A', '87654321B,11223344C', 'ABC1234'),
(1, '2025-08-05', '2025-08-12', 900.50, 3, '26325825W', '33445566D', 'XYZ9876'),
(2, '2025-09-15', '2025-09-20', 650.00, 2, '26325825W', NULL, 'LMN2468');


-- --------------------------------------------------------
-- 
-- Estructura de tabla para la tabla `reserva`
-- 
CREATE TABLE `reserva` (
  `id` INT(11) NOT NULL,
  `fecha` DATE NOT NULL,
  `plazas` INT(11) NOT NULL,
  `precio` DOUBLE NOT NULL,
  `usuario_id` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `matricula_embarcacion` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `descripcion` TEXT COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `reserva`
-- 
INSERT INTO `reserva` VALUES 
(0, '2025-06-01', 4, 350.00, '12345678A', 'ABC1234', 'Reserva de embarcación para paseo familiar.'),
(1, '2025-06-15', 2, 200.00, '26325825W', 'XYZ9876', 'Reserva para pesca deportiva.'),
(2, '2025-07-20', 6, 500.00, '26325825W', 'LMN2468', 'Salida de grupo turístico.');
-- 
-- Base de datos: `information_schema`
-- 
CREATE DATABASE `information_schema` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE information_schema;

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `CHARACTER_SETS`
-- 

CREATE TEMPORARY TABLE `CHARACTER_SETS` (
  `CHARACTER_SET_NAME` varchar(32) NOT NULL DEFAULT '',
  `DEFAULT_COLLATE_NAME` varchar(32) NOT NULL DEFAULT '',
  `DESCRIPTION` varchar(60) NOT NULL DEFAULT '',
  `MAXLEN` bigint(3) NOT NULL DEFAULT '0'
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `CHARACTER_SETS`
-- 

INSERT INTO `CHARACTER_SETS` VALUES ('big5', 'big5_chinese_ci', 'Big5 Traditional Chinese', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('dec8', 'dec8_swedish_ci', 'DEC West European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp850', 'cp850_general_ci', 'DOS West European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('hp8', 'hp8_english_ci', 'HP West European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('koi8r', 'koi8r_general_ci', 'KOI8-R Relcom Russian', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('latin1', 'latin1_swedish_ci', 'cp1252 West European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('latin2', 'latin2_general_ci', 'ISO 8859-2 Central European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('swe7', 'swe7_swedish_ci', '7bit Swedish', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('ascii', 'ascii_general_ci', 'US ASCII', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('ujis', 'ujis_japanese_ci', 'EUC-JP Japanese', 3);
INSERT INTO `CHARACTER_SETS` VALUES ('sjis', 'sjis_japanese_ci', 'Shift-JIS Japanese', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('hebrew', 'hebrew_general_ci', 'ISO 8859-8 Hebrew', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('tis620', 'tis620_thai_ci', 'TIS620 Thai', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('euckr', 'euckr_korean_ci', 'EUC-KR Korean', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('koi8u', 'koi8u_general_ci', 'KOI8-U Ukrainian', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('gb2312', 'gb2312_chinese_ci', 'GB2312 Simplified Chinese', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('greek', 'greek_general_ci', 'ISO 8859-7 Greek', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp1250', 'cp1250_general_ci', 'Windows Central European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('gbk', 'gbk_chinese_ci', 'GBK Simplified Chinese', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('latin5', 'latin5_turkish_ci', 'ISO 8859-9 Turkish', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('armscii8', 'armscii8_general_ci', 'ARMSCII-8 Armenian', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('utf8', 'utf8_general_ci', 'UTF-8 Unicode', 3);
INSERT INTO `CHARACTER_SETS` VALUES ('ucs2', 'ucs2_general_ci', 'UCS-2 Unicode', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('cp866', 'cp866_general_ci', 'DOS Russian', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('keybcs2', 'keybcs2_general_ci', 'DOS Kamenicky Czech-Slovak', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('macce', 'macce_general_ci', 'Mac Central European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('macroman', 'macroman_general_ci', 'Mac West European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp852', 'cp852_general_ci', 'DOS Central European', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('latin7', 'latin7_general_ci', 'ISO 8859-13 Baltic', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp1251', 'cp1251_general_ci', 'Windows Cyrillic', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp1256', 'cp1256_general_ci', 'Windows Arabic', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp1257', 'cp1257_general_ci', 'Windows Baltic', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('binary', 'binary', 'Binary pseudo charset', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('geostd8', 'geostd8_general_ci', 'GEOSTD8 Georgian', 1);
INSERT INTO `CHARACTER_SETS` VALUES ('cp932', 'cp932_japanese_ci', 'SJIS for Windows Japanese', 2);
INSERT INTO `CHARACTER_SETS` VALUES ('eucjpms', 'eucjpms_japanese_ci', 'UJIS for Windows Japanese', 3);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `COLLATIONS`
-- 

CREATE TEMPORARY TABLE `COLLATIONS` (
  `COLLATION_NAME` varchar(32) NOT NULL DEFAULT '',
  `CHARACTER_SET_NAME` varchar(32) NOT NULL DEFAULT '',
  `ID` bigint(11) NOT NULL DEFAULT '0',
  `IS_DEFAULT` varchar(3) NOT NULL DEFAULT '',
  `IS_COMPILED` varchar(3) NOT NULL DEFAULT '',
  `SORTLEN` bigint(3) NOT NULL DEFAULT '0'
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `COLLATIONS`
-- 

INSERT INTO `COLLATIONS` VALUES ('big5_chinese_ci', 'big5', 1, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('big5_bin', 'big5', 84, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('dec8_swedish_ci', 'dec8', 3, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('dec8_bin', 'dec8', 69, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp850_general_ci', 'cp850', 4, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp850_bin', 'cp850', 80, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('hp8_english_ci', 'hp8', 6, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('hp8_bin', 'hp8', 72, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('koi8r_general_ci', 'koi8r', 7, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('koi8r_bin', 'koi8r', 74, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_german1_ci', 'latin1', 5, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_swedish_ci', 'latin1', 8, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_danish_ci', 'latin1', 15, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_german2_ci', 'latin1', 31, '', 'Yes', 2);
INSERT INTO `COLLATIONS` VALUES ('latin1_bin', 'latin1', 47, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_general_ci', 'latin1', 48, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_general_cs', 'latin1', 49, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin1_spanish_ci', 'latin1', 94, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin2_czech_cs', 'latin2', 2, '', 'Yes', 4);
INSERT INTO `COLLATIONS` VALUES ('latin2_general_ci', 'latin2', 9, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin2_hungarian_ci', 'latin2', 21, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin2_croatian_ci', 'latin2', 27, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin2_bin', 'latin2', 77, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('swe7_swedish_ci', 'swe7', 10, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('swe7_bin', 'swe7', 82, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ascii_general_ci', 'ascii', 11, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ascii_bin', 'ascii', 65, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ujis_japanese_ci', 'ujis', 12, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ujis_bin', 'ujis', 91, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('sjis_japanese_ci', 'sjis', 13, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('sjis_bin', 'sjis', 88, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('hebrew_general_ci', 'hebrew', 16, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('hebrew_bin', 'hebrew', 71, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('tis620_thai_ci', 'tis620', 18, 'Yes', 'Yes', 4);
INSERT INTO `COLLATIONS` VALUES ('tis620_bin', 'tis620', 89, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('euckr_korean_ci', 'euckr', 19, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('euckr_bin', 'euckr', 85, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('koi8u_general_ci', 'koi8u', 22, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('koi8u_bin', 'koi8u', 75, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('gb2312_chinese_ci', 'gb2312', 24, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('gb2312_bin', 'gb2312', 86, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('greek_general_ci', 'greek', 25, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('greek_bin', 'greek', 70, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1250_general_ci', 'cp1250', 26, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1250_czech_cs', 'cp1250', 34, '', 'Yes', 2);
INSERT INTO `COLLATIONS` VALUES ('cp1250_croatian_ci', 'cp1250', 44, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1250_bin', 'cp1250', 66, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1250_polish_ci', 'cp1250', 99, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('gbk_chinese_ci', 'gbk', 28, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('gbk_bin', 'gbk', 87, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin5_turkish_ci', 'latin5', 30, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin5_bin', 'latin5', 78, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('armscii8_general_ci', 'armscii8', 32, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('armscii8_bin', 'armscii8', 64, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('utf8_general_ci', 'utf8', 33, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('utf8_bin', 'utf8', 83, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('utf8_unicode_ci', 'utf8', 192, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_icelandic_ci', 'utf8', 193, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_latvian_ci', 'utf8', 194, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_romanian_ci', 'utf8', 195, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_slovenian_ci', 'utf8', 196, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_polish_ci', 'utf8', 197, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_estonian_ci', 'utf8', 198, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_spanish_ci', 'utf8', 199, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_swedish_ci', 'utf8', 200, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_turkish_ci', 'utf8', 201, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_czech_ci', 'utf8', 202, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_danish_ci', 'utf8', 203, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_lithuanian_ci', 'utf8', 204, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_slovak_ci', 'utf8', 205, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_spanish2_ci', 'utf8', 206, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_roman_ci', 'utf8', 207, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_persian_ci', 'utf8', 208, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_esperanto_ci', 'utf8', 209, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_hungarian_ci', 'utf8', 210, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('utf8_general_mysql500_ci', 'utf8', 223, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ucs2_general_ci', 'ucs2', 35, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ucs2_bin', 'ucs2', 90, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('ucs2_unicode_ci', 'ucs2', 128, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_icelandic_ci', 'ucs2', 129, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_latvian_ci', 'ucs2', 130, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_romanian_ci', 'ucs2', 131, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_slovenian_ci', 'ucs2', 132, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_polish_ci', 'ucs2', 133, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_estonian_ci', 'ucs2', 134, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_spanish_ci', 'ucs2', 135, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_swedish_ci', 'ucs2', 136, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_turkish_ci', 'ucs2', 137, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_czech_ci', 'ucs2', 138, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_danish_ci', 'ucs2', 139, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_lithuanian_ci', 'ucs2', 140, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_slovak_ci', 'ucs2', 141, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_spanish2_ci', 'ucs2', 142, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_roman_ci', 'ucs2', 143, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_persian_ci', 'ucs2', 144, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_esperanto_ci', 'ucs2', 145, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_hungarian_ci', 'ucs2', 146, '', 'Yes', 8);
INSERT INTO `COLLATIONS` VALUES ('ucs2_general_mysql500_ci', 'ucs2', 159, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp866_general_ci', 'cp866', 36, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp866_bin', 'cp866', 68, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('keybcs2_general_ci', 'keybcs2', 37, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('keybcs2_bin', 'keybcs2', 73, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('macce_general_ci', 'macce', 38, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('macce_bin', 'macce', 43, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('macroman_general_ci', 'macroman', 39, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('macroman_bin', 'macroman', 53, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp852_general_ci', 'cp852', 40, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp852_bin', 'cp852', 81, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin7_estonian_cs', 'latin7', 20, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin7_general_ci', 'latin7', 41, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin7_general_cs', 'latin7', 42, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('latin7_bin', 'latin7', 79, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1251_bulgarian_ci', 'cp1251', 14, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1251_ukrainian_ci', 'cp1251', 23, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1251_bin', 'cp1251', 50, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1251_general_ci', 'cp1251', 51, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1251_general_cs', 'cp1251', 52, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1256_general_ci', 'cp1256', 57, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1256_bin', 'cp1256', 67, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1257_lithuanian_ci', 'cp1257', 29, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1257_bin', 'cp1257', 58, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp1257_general_ci', 'cp1257', 59, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('binary', 'binary', 63, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('geostd8_general_ci', 'geostd8', 92, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('geostd8_bin', 'geostd8', 93, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp932_japanese_ci', 'cp932', 95, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('cp932_bin', 'cp932', 96, '', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('eucjpms_japanese_ci', 'eucjpms', 97, 'Yes', 'Yes', 1);
INSERT INTO `COLLATIONS` VALUES ('eucjpms_bin', 'eucjpms', 98, '', 'Yes', 1);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `COLLATION_CHARACTER_SET_APPLICABILITY`
-- 

CREATE TEMPORARY TABLE `COLLATION_CHARACTER_SET_APPLICABILITY` (
  `COLLATION_NAME` varchar(32) NOT NULL DEFAULT '',
  `CHARACTER_SET_NAME` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `COLLATION_CHARACTER_SET_APPLICABILITY`
-- 

INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('big5_chinese_ci', 'big5');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('big5_bin', 'big5');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('dec8_swedish_ci', 'dec8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('dec8_bin', 'dec8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp850_general_ci', 'cp850');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp850_bin', 'cp850');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('hp8_english_ci', 'hp8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('hp8_bin', 'hp8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('koi8r_general_ci', 'koi8r');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('koi8r_bin', 'koi8r');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_german1_ci', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_swedish_ci', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_danish_ci', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_german2_ci', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_bin', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_general_ci', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_general_cs', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin1_spanish_ci', 'latin1');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin2_czech_cs', 'latin2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin2_general_ci', 'latin2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin2_hungarian_ci', 'latin2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin2_croatian_ci', 'latin2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin2_bin', 'latin2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('swe7_swedish_ci', 'swe7');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('swe7_bin', 'swe7');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ascii_general_ci', 'ascii');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ascii_bin', 'ascii');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ujis_japanese_ci', 'ujis');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ujis_bin', 'ujis');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('sjis_japanese_ci', 'sjis');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('sjis_bin', 'sjis');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('hebrew_general_ci', 'hebrew');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('hebrew_bin', 'hebrew');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('filename', 'filename');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('tis620_thai_ci', 'tis620');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('tis620_bin', 'tis620');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('euckr_korean_ci', 'euckr');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('euckr_bin', 'euckr');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('koi8u_general_ci', 'koi8u');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('koi8u_bin', 'koi8u');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('gb2312_chinese_ci', 'gb2312');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('gb2312_bin', 'gb2312');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('greek_general_ci', 'greek');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('greek_bin', 'greek');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1250_general_ci', 'cp1250');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1250_czech_cs', 'cp1250');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1250_croatian_ci', 'cp1250');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1250_bin', 'cp1250');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1250_polish_ci', 'cp1250');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('gbk_chinese_ci', 'gbk');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('gbk_bin', 'gbk');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin5_turkish_ci', 'latin5');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin5_bin', 'latin5');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('armscii8_general_ci', 'armscii8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('armscii8_bin', 'armscii8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_general_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_bin', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_unicode_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_icelandic_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_latvian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_romanian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_slovenian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_polish_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_estonian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_spanish_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_swedish_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_turkish_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_czech_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_danish_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_lithuanian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_slovak_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_spanish2_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_roman_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_persian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_esperanto_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_hungarian_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('utf8_general_mysql500_ci', 'utf8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_general_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_bin', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_unicode_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_icelandic_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_latvian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_romanian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_slovenian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_polish_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_estonian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_spanish_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_swedish_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_turkish_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_czech_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_danish_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_lithuanian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_slovak_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_spanish2_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_roman_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_persian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_esperanto_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_hungarian_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('ucs2_general_mysql500_ci', 'ucs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp866_general_ci', 'cp866');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp866_bin', 'cp866');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('keybcs2_general_ci', 'keybcs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('keybcs2_bin', 'keybcs2');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('macce_general_ci', 'macce');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('macce_bin', 'macce');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('macroman_general_ci', 'macroman');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('macroman_bin', 'macroman');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp852_general_ci', 'cp852');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp852_bin', 'cp852');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin7_estonian_cs', 'latin7');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin7_general_ci', 'latin7');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin7_general_cs', 'latin7');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('latin7_bin', 'latin7');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1251_bulgarian_ci', 'cp1251');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1251_ukrainian_ci', 'cp1251');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1251_bin', 'cp1251');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1251_general_ci', 'cp1251');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1251_general_cs', 'cp1251');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1256_general_ci', 'cp1256');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1256_bin', 'cp1256');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1257_lithuanian_ci', 'cp1257');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1257_bin', 'cp1257');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp1257_general_ci', 'cp1257');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('binary', 'binary');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('geostd8_general_ci', 'geostd8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('geostd8_bin', 'geostd8');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp932_japanese_ci', 'cp932');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('cp932_bin', 'cp932');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('eucjpms_japanese_ci', 'eucjpms');
INSERT INTO `COLLATION_CHARACTER_SET_APPLICABILITY` VALUES ('eucjpms_bin', 'eucjpms');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `COLUMNS`
-- 

CREATE TEMPORARY TABLE `COLUMNS` (
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `COLUMN_NAME` varchar(64) NOT NULL DEFAULT '',
  `ORDINAL_POSITION` bigint(21) unsigned NOT NULL DEFAULT '0',
  `COLUMN_DEFAULT` longtext,
  `IS_NULLABLE` varchar(3) NOT NULL DEFAULT '',
  `DATA_TYPE` varchar(64) NOT NULL DEFAULT '',
  `CHARACTER_MAXIMUM_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `CHARACTER_OCTET_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `NUMERIC_PRECISION` bigint(21) unsigned DEFAULT NULL,
  `NUMERIC_SCALE` bigint(21) unsigned DEFAULT NULL,
  `CHARACTER_SET_NAME` varchar(32) DEFAULT NULL,
  `COLLATION_NAME` varchar(32) DEFAULT NULL,
  `COLUMN_TYPE` longtext NOT NULL,
  `COLUMN_KEY` varchar(3) NOT NULL DEFAULT '',
  `EXTRA` varchar(27) NOT NULL DEFAULT '',
  `PRIVILEGES` varchar(80) NOT NULL DEFAULT '',
  `COLUMN_COMMENT` varchar(255) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `COLUMNS`
-- 

INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'CHARACTER_SETS', 'CHARACTER_SET_NAME', 1, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'CHARACTER_SETS', 'DEFAULT_COLLATE_NAME', 2, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'CHARACTER_SETS', 'DESCRIPTION', 3, '', 'NO', 'varchar', 60, 180, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(60)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'CHARACTER_SETS', 'MAXLEN', 4, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATIONS', 'COLLATION_NAME', 1, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATIONS', 'CHARACTER_SET_NAME', 2, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATIONS', 'ID', 3, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(11)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATIONS', 'IS_DEFAULT', 4, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATIONS', 'IS_COMPILED', 5, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATIONS', 'SORTLEN', 6, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATION_CHARACTER_SET_APPLICABILITY', 'COLLATION_NAME', 1, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLLATION_CHARACTER_SET_APPLICABILITY', 'CHARACTER_SET_NAME', 2, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'TABLE_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'TABLE_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'TABLE_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'COLUMN_NAME', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'ORDINAL_POSITION', 5, '0', 'NO', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'COLUMN_DEFAULT', 6, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'IS_NULLABLE', 7, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'DATA_TYPE', 8, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'CHARACTER_MAXIMUM_LENGTH', 9, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'CHARACTER_OCTET_LENGTH', 10, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'NUMERIC_PRECISION', 11, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'NUMERIC_SCALE', 12, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'CHARACTER_SET_NAME', 13, NULL, 'YES', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'COLLATION_NAME', 14, NULL, 'YES', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'COLUMN_TYPE', 15, NULL, 'NO', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'COLUMN_KEY', 16, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'EXTRA', 17, '', 'NO', 'varchar', 27, 81, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(27)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'PRIVILEGES', 18, '', 'NO', 'varchar', 80, 240, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(80)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMNS', 'COLUMN_COMMENT', 19, '', 'NO', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(255)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'GRANTEE', 1, '', 'NO', 'varchar', 81, 243, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(81)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'TABLE_CATALOG', 2, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'TABLE_SCHEMA', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'TABLE_NAME', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'COLUMN_NAME', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'PRIVILEGE_TYPE', 6, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'IS_GRANTABLE', 7, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ENGINES', 'ENGINE', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ENGINES', 'SUPPORT', 2, '', 'NO', 'varchar', 8, 24, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ENGINES', 'COMMENT', 3, '', 'NO', 'varchar', 80, 240, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(80)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ENGINES', 'TRANSACTIONS', 4, NULL, 'YES', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ENGINES', 'XA', 5, NULL, 'YES', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ENGINES', 'SAVEPOINTS', 6, NULL, 'YES', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_CATALOG', 1, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'DEFINER', 4, '', 'NO', 'varchar', 77, 231, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(77)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'TIME_ZONE', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_BODY', 6, '', 'NO', 'varchar', 8, 24, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_DEFINITION', 7, NULL, 'NO', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_TYPE', 8, '', 'NO', 'varchar', 9, 27, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(9)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EXECUTE_AT', 9, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'INTERVAL_VALUE', 10, NULL, 'YES', 'varchar', 256, 768, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(256)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'INTERVAL_FIELD', 11, NULL, 'YES', 'varchar', 18, 54, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(18)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'SQL_MODE', 12, '', 'NO', 'varchar', 8192, 24576, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8192)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'STARTS', 13, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'ENDS', 14, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'STATUS', 15, '', 'NO', 'varchar', 18, 54, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(18)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'ON_COMPLETION', 16, '', 'NO', 'varchar', 12, 36, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(12)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'CREATED', 17, '0000-00-00 00:00:00', 'NO', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'LAST_ALTERED', 18, '0000-00-00 00:00:00', 'NO', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'LAST_EXECUTED', 19, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'EVENT_COMMENT', 20, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'ORIGINATOR', 21, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'CHARACTER_SET_CLIENT', 22, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'COLLATION_CONNECTION', 23, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'EVENTS', 'DATABASE_COLLATION', 24, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'FILE_ID', 1, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'FILE_NAME', 2, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'FILE_TYPE', 3, '', 'NO', 'varchar', 20, 60, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TABLESPACE_NAME', 4, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TABLE_CATALOG', 5, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TABLE_SCHEMA', 6, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TABLE_NAME', 7, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'LOGFILE_GROUP_NAME', 8, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'LOGFILE_GROUP_NUMBER', 9, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'ENGINE', 10, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'FULLTEXT_KEYS', 11, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'DELETED_ROWS', 12, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'UPDATE_COUNT', 13, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'FREE_EXTENTS', 14, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TOTAL_EXTENTS', 15, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'EXTENT_SIZE', 16, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'INITIAL_SIZE', 17, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'MAXIMUM_SIZE', 18, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'AUTOEXTEND_SIZE', 19, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'CREATION_TIME', 20, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'LAST_UPDATE_TIME', 21, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'LAST_ACCESS_TIME', 22, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'RECOVER_TIME', 23, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TRANSACTION_COUNTER', 24, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'VERSION', 25, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'ROW_FORMAT', 26, NULL, 'YES', 'varchar', 10, 30, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'TABLE_ROWS', 27, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'AVG_ROW_LENGTH', 28, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'DATA_LENGTH', 29, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'MAX_DATA_LENGTH', 30, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'INDEX_LENGTH', 31, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'DATA_FREE', 32, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'CREATE_TIME', 33, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'UPDATE_TIME', 34, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'CHECK_TIME', 35, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'CHECKSUM', 36, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'STATUS', 37, '', 'NO', 'varchar', 20, 60, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'FILES', 'EXTRA', 38, NULL, 'YES', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(255)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'GLOBAL_STATUS', 'VARIABLE_NAME', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'GLOBAL_STATUS', 'VARIABLE_VALUE', 2, NULL, 'YES', 'varchar', 1024, 3072, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(1024)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'GLOBAL_VARIABLES', 'VARIABLE_NAME', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'GLOBAL_VARIABLES', 'VARIABLE_VALUE', 2, NULL, 'YES', 'varchar', 1024, 3072, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(1024)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'CONSTRAINT_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'CONSTRAINT_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'CONSTRAINT_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'TABLE_CATALOG', 4, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'TABLE_SCHEMA', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'TABLE_NAME', 6, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'COLUMN_NAME', 7, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'ORDINAL_POSITION', 8, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'POSITION_IN_UNIQUE_CONSTRAINT', 9, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'REFERENCED_TABLE_SCHEMA', 10, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'REFERENCED_TABLE_NAME', 11, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'REFERENCED_COLUMN_NAME', 12, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'TABLE_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'TABLE_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'TABLE_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'PARTITION_NAME', 4, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'SUBPARTITION_NAME', 5, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'PARTITION_ORDINAL_POSITION', 6, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'SUBPARTITION_ORDINAL_POSITION', 7, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'PARTITION_METHOD', 8, NULL, 'YES', 'varchar', 12, 36, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(12)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'SUBPARTITION_METHOD', 9, NULL, 'YES', 'varchar', 12, 36, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(12)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'PARTITION_EXPRESSION', 10, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'SUBPARTITION_EXPRESSION', 11, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'PARTITION_DESCRIPTION', 12, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'TABLE_ROWS', 13, '0', 'NO', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'AVG_ROW_LENGTH', 14, '0', 'NO', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'DATA_LENGTH', 15, '0', 'NO', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'MAX_DATA_LENGTH', 16, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'INDEX_LENGTH', 17, '0', 'NO', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'DATA_FREE', 18, '0', 'NO', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'CREATE_TIME', 19, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'UPDATE_TIME', 20, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'CHECK_TIME', 21, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'CHECKSUM', 22, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'PARTITION_COMMENT', 23, '', 'NO', 'varchar', 80, 240, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(80)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'NODEGROUP', 24, '', 'NO', 'varchar', 12, 36, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(12)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PARTITIONS', 'TABLESPACE_NAME', 25, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_NAME', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_VERSION', 2, '', 'NO', 'varchar', 20, 60, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_STATUS', 3, '', 'NO', 'varchar', 10, 30, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_TYPE', 4, '', 'NO', 'varchar', 80, 240, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(80)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_TYPE_VERSION', 5, '', 'NO', 'varchar', 20, 60, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_LIBRARY', 6, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_LIBRARY_VERSION', 7, NULL, 'YES', 'varchar', 20, 60, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_AUTHOR', 8, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_DESCRIPTION', 9, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PLUGINS', 'PLUGIN_LICENSE', 10, NULL, 'YES', 'varchar', 80, 240, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(80)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'ID', 1, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'USER', 2, '', 'NO', 'varchar', 16, 48, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(16)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'HOST', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'DB', 4, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'COMMAND', 5, '', 'NO', 'varchar', 16, 48, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(16)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'TIME', 6, '0', 'NO', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(7)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'STATE', 7, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'INFO', 8, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'QUERY_ID', 1, '0', 'NO', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'SEQ', 2, '0', 'NO', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'STATE', 3, '', 'NO', 'varchar', 30, 90, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(30)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'DURATION', 4, '0.000000', 'NO', 'decimal', NULL, NULL, 9, 6, NULL, NULL, 'decimal(9,6)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'CPU_USER', 5, NULL, 'YES', 'decimal', NULL, NULL, 9, 6, NULL, NULL, 'decimal(9,6)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'CPU_SYSTEM', 6, NULL, 'YES', 'decimal', NULL, NULL, 9, 6, NULL, NULL, 'decimal(9,6)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'CONTEXT_VOLUNTARY', 7, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'CONTEXT_INVOLUNTARY', 8, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'BLOCK_OPS_IN', 9, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'BLOCK_OPS_OUT', 10, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'MESSAGES_SENT', 11, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'MESSAGES_RECEIVED', 12, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'PAGE_FAULTS_MAJOR', 13, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'PAGE_FAULTS_MINOR', 14, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'SWAPS', 15, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'SOURCE_FUNCTION', 16, NULL, 'YES', 'varchar', 30, 90, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(30)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'SOURCE_FILE', 17, NULL, 'YES', 'varchar', 20, 60, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'PROFILING', 'SOURCE_LINE', 18, NULL, 'YES', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(20)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'CONSTRAINT_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'CONSTRAINT_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'CONSTRAINT_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'UNIQUE_CONSTRAINT_CATALOG', 4, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'UNIQUE_CONSTRAINT_SCHEMA', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'UNIQUE_CONSTRAINT_NAME', 6, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'MATCH_OPTION', 7, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'UPDATE_RULE', 8, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'DELETE_RULE', 9, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'TABLE_NAME', 10, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'REFERENCED_TABLE_NAME', 11, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'SPECIFIC_NAME', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_CATALOG', 2, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_SCHEMA', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_NAME', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_TYPE', 5, '', 'NO', 'varchar', 9, 27, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(9)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'DTD_IDENTIFIER', 6, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_BODY', 7, '', 'NO', 'varchar', 8, 24, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_DEFINITION', 8, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'EXTERNAL_NAME', 9, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'EXTERNAL_LANGUAGE', 10, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'PARAMETER_STYLE', 11, '', 'NO', 'varchar', 8, 24, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'IS_DETERMINISTIC', 12, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'SQL_DATA_ACCESS', 13, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'SQL_PATH', 14, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'SECURITY_TYPE', 15, '', 'NO', 'varchar', 7, 21, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(7)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'CREATED', 16, '0000-00-00 00:00:00', 'NO', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'LAST_ALTERED', 17, '0000-00-00 00:00:00', 'NO', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'SQL_MODE', 18, '', 'NO', 'varchar', 8192, 24576, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8192)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'ROUTINE_COMMENT', 19, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'DEFINER', 20, '', 'NO', 'varchar', 77, 231, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(77)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'CHARACTER_SET_CLIENT', 21, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'COLLATION_CONNECTION', 22, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'ROUTINES', 'DATABASE_COLLATION', 23, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMATA', 'CATALOG_NAME', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMATA', 'SCHEMA_NAME', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMATA', 'DEFAULT_CHARACTER_SET_NAME', 3, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMATA', 'DEFAULT_COLLATION_NAME', 4, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMATA', 'SQL_PATH', 5, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', 'GRANTEE', 1, '', 'NO', 'varchar', 81, 243, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(81)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', 'TABLE_CATALOG', 2, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', 'TABLE_SCHEMA', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', 'PRIVILEGE_TYPE', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', 'IS_GRANTABLE', 5, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SESSION_STATUS', 'VARIABLE_NAME', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SESSION_STATUS', 'VARIABLE_VALUE', 2, NULL, 'YES', 'varchar', 1024, 3072, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(1024)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SESSION_VARIABLES', 'VARIABLE_NAME', 1, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'SESSION_VARIABLES', 'VARIABLE_VALUE', 2, NULL, 'YES', 'varchar', 1024, 3072, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(1024)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'TABLE_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'TABLE_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'TABLE_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'NON_UNIQUE', 4, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(1)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'INDEX_SCHEMA', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'INDEX_NAME', 6, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'SEQ_IN_INDEX', 7, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(2)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'COLUMN_NAME', 8, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'COLLATION', 9, NULL, 'YES', 'varchar', 1, 3, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(1)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'CARDINALITY', 10, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(21)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'SUB_PART', 11, NULL, 'YES', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'PACKED', 12, NULL, 'YES', 'varchar', 10, 30, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'NULLABLE', 13, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'INDEX_TYPE', 14, '', 'NO', 'varchar', 16, 48, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(16)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'STATISTICS', 'COMMENT', 15, NULL, 'YES', 'varchar', 16, 48, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(16)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_TYPE', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'ENGINE', 5, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'VERSION', 6, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'ROW_FORMAT', 7, NULL, 'YES', 'varchar', 10, 30, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(10)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_ROWS', 8, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'AVG_ROW_LENGTH', 9, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'DATA_LENGTH', 10, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'MAX_DATA_LENGTH', 11, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'INDEX_LENGTH', 12, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'DATA_FREE', 13, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'AUTO_INCREMENT', 14, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'CREATE_TIME', 15, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'UPDATE_TIME', 16, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'CHECK_TIME', 17, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_COLLATION', 18, NULL, 'YES', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'CHECKSUM', 19, NULL, 'YES', 'bigint', NULL, NULL, 20, 0, NULL, NULL, 'bigint(21) unsigned', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'CREATE_OPTIONS', 20, NULL, 'YES', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(255)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLES', 'TABLE_COMMENT', 21, '', 'NO', 'varchar', 80, 240, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(80)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'CONSTRAINT_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'CONSTRAINT_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'CONSTRAINT_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'TABLE_SCHEMA', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'TABLE_NAME', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'CONSTRAINT_TYPE', 6, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'GRANTEE', 1, '', 'NO', 'varchar', 81, 243, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(81)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'TABLE_CATALOG', 2, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'TABLE_SCHEMA', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'TABLE_NAME', 4, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'PRIVILEGE_TYPE', 5, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'IS_GRANTABLE', 6, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'TRIGGER_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'TRIGGER_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'TRIGGER_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'EVENT_MANIPULATION', 4, '', 'NO', 'varchar', 6, 18, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(6)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'EVENT_OBJECT_CATALOG', 5, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'EVENT_OBJECT_SCHEMA', 6, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'EVENT_OBJECT_TABLE', 7, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_ORDER', 8, '0', 'NO', 'bigint', NULL, NULL, 19, 0, NULL, NULL, 'bigint(4)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_CONDITION', 9, NULL, 'YES', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_STATEMENT', 10, NULL, 'NO', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_ORIENTATION', 11, '', 'NO', 'varchar', 9, 27, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(9)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_TIMING', 12, '', 'NO', 'varchar', 6, 18, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(6)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_REFERENCE_OLD_TABLE', 13, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_REFERENCE_NEW_TABLE', 14, NULL, 'YES', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_REFERENCE_OLD_ROW', 15, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'ACTION_REFERENCE_NEW_ROW', 16, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'CREATED', 17, NULL, 'YES', 'datetime', NULL, NULL, NULL, NULL, NULL, NULL, 'datetime', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'SQL_MODE', 18, '', 'NO', 'varchar', 8192, 24576, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8192)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'DEFINER', 19, '', 'NO', 'varchar', 77, 231, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(77)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'CHARACTER_SET_CLIENT', 20, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'COLLATION_CONNECTION', 21, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'TRIGGERS', 'DATABASE_COLLATION', 22, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'USER_PRIVILEGES', 'GRANTEE', 1, '', 'NO', 'varchar', 81, 243, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(81)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'USER_PRIVILEGES', 'TABLE_CATALOG', 2, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'USER_PRIVILEGES', 'PRIVILEGE_TYPE', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'USER_PRIVILEGES', 'IS_GRANTABLE', 4, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'TABLE_CATALOG', 1, NULL, 'YES', 'varchar', 512, 1536, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(512)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'TABLE_SCHEMA', 2, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'TABLE_NAME', 3, '', 'NO', 'varchar', 64, 192, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(64)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'VIEW_DEFINITION', 4, NULL, 'NO', 'longtext', 4294967295, 4294967295, NULL, NULL, 'utf8', 'utf8_general_ci', 'longtext', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'CHECK_OPTION', 5, '', 'NO', 'varchar', 8, 24, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(8)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'IS_UPDATABLE', 6, '', 'NO', 'varchar', 3, 9, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(3)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'DEFINER', 7, '', 'NO', 'varchar', 77, 231, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(77)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'SECURITY_TYPE', 8, '', 'NO', 'varchar', 7, 21, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(7)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'CHARACTER_SET_CLIENT', 9, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'information_schema', 'VIEWS', 'COLLATION_CONNECTION', 10, '', 'NO', 'varchar', 32, 96, NULL, NULL, 'utf8', 'utf8_general_ci', 'varchar(32)', '', '', 'select', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'id', 1, NULL, 'NO', 'int', NULL, NULL, 10, 0, NULL, NULL, 'int(11)', 'PRI', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'nombre', 2, NULL, 'NO', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_unicode_ci', 'varchar(255)', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'apellidos', 3, NULL, 'NO', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_unicode_ci', 'varchar(255)', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'dni', 4, NULL, 'NO', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_unicode_ci', 'varchar(255)', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'fecha_nacimiento', 5, NULL, 'YES', 'date', NULL, NULL, NULL, NULL, NULL, NULL, 'date', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'direccion', 6, NULL, 'NO', 'varchar', 255, 765, NULL, NULL, 'utf8', 'utf8_unicode_ci', 'varchar(255)', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'fecha_inscripcion', 7, 'CURRENT_TIMESTAMP', 'NO', 'timestamp', NULL, NULL, NULL, NULL, NULL, NULL, 'timestamp', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'es_titular', 8, '0', 'NO', 'tinyint', NULL, NULL, 3, 0, NULL, NULL, 'tinyint(1)', '', '', 'select,insert,update', '');
INSERT INTO `COLUMNS` VALUES (NULL, 'i32esrah', 'socios', 'titulo_patron', 9, '0', 'YES', 'tinyint', NULL, NULL, 3, 0, NULL, NULL, 'tinyint(1)', '', '', 'select,insert,update', '');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `COLUMN_PRIVILEGES`
-- 

CREATE TEMPORARY TABLE `COLUMN_PRIVILEGES` (
  `GRANTEE` varchar(81) NOT NULL DEFAULT '',
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `COLUMN_NAME` varchar(64) NOT NULL DEFAULT '',
  `PRIVILEGE_TYPE` varchar(64) NOT NULL DEFAULT '',
  `IS_GRANTABLE` varchar(3) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `COLUMN_PRIVILEGES`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `ENGINES`
-- 

CREATE TEMPORARY TABLE `ENGINES` (
  `ENGINE` varchar(64) NOT NULL DEFAULT '',
  `SUPPORT` varchar(8) NOT NULL DEFAULT '',
  `COMMENT` varchar(80) NOT NULL DEFAULT '',
  `TRANSACTIONS` varchar(3) DEFAULT NULL,
  `XA` varchar(3) DEFAULT NULL,
  `SAVEPOINTS` varchar(3) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `ENGINES`
-- 

INSERT INTO `ENGINES` VALUES ('MRG_MYISAM', 'YES', 'Collection of identical MyISAM tables', 'NO', 'NO', 'NO');
INSERT INTO `ENGINES` VALUES ('CSV', 'YES', 'CSV storage engine', 'NO', 'NO', 'NO');
INSERT INTO `ENGINES` VALUES ('MyISAM', 'DEFAULT', 'Default engine as of MySQL 3.23 with great performance', 'NO', 'NO', 'NO');
INSERT INTO `ENGINES` VALUES ('InnoDB', 'YES', 'Supports transactions, row-level locking, and foreign keys', 'YES', 'YES', 'YES');
INSERT INTO `ENGINES` VALUES ('MEMORY', 'YES', 'Hash based, stored in memory, useful for temporary tables', 'NO', 'NO', 'NO');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `EVENTS`
-- 

CREATE TEMPORARY TABLE `EVENTS` (
  `EVENT_CATALOG` varchar(64) DEFAULT NULL,
  `EVENT_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `EVENT_NAME` varchar(64) NOT NULL DEFAULT '',
  `DEFINER` varchar(77) NOT NULL DEFAULT '',
  `TIME_ZONE` varchar(64) NOT NULL DEFAULT '',
  `EVENT_BODY` varchar(8) NOT NULL DEFAULT '',
  `EVENT_DEFINITION` longtext NOT NULL,
  `EVENT_TYPE` varchar(9) NOT NULL DEFAULT '',
  `EXECUTE_AT` datetime DEFAULT NULL,
  `INTERVAL_VALUE` varchar(256) DEFAULT NULL,
  `INTERVAL_FIELD` varchar(18) DEFAULT NULL,
  `SQL_MODE` varchar(8192) NOT NULL DEFAULT '',
  `STARTS` datetime DEFAULT NULL,
  `ENDS` datetime DEFAULT NULL,
  `STATUS` varchar(18) NOT NULL DEFAULT '',
  `ON_COMPLETION` varchar(12) NOT NULL DEFAULT '',
  `CREATED` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LAST_ALTERED` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LAST_EXECUTED` datetime DEFAULT NULL,
  `EVENT_COMMENT` varchar(64) NOT NULL DEFAULT '',
  `ORIGINATOR` bigint(10) NOT NULL DEFAULT '0',
  `CHARACTER_SET_CLIENT` varchar(32) NOT NULL DEFAULT '',
  `COLLATION_CONNECTION` varchar(32) NOT NULL DEFAULT '',
  `DATABASE_COLLATION` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `EVENTS`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `FILES`
-- 

CREATE TEMPORARY TABLE `FILES` (
  `FILE_ID` bigint(4) NOT NULL DEFAULT '0',
  `FILE_NAME` varchar(64) DEFAULT NULL,
  `FILE_TYPE` varchar(20) NOT NULL DEFAULT '',
  `TABLESPACE_NAME` varchar(64) DEFAULT NULL,
  `TABLE_CATALOG` varchar(64) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) DEFAULT NULL,
  `TABLE_NAME` varchar(64) DEFAULT NULL,
  `LOGFILE_GROUP_NAME` varchar(64) DEFAULT NULL,
  `LOGFILE_GROUP_NUMBER` bigint(4) DEFAULT NULL,
  `ENGINE` varchar(64) NOT NULL DEFAULT '',
  `FULLTEXT_KEYS` varchar(64) DEFAULT NULL,
  `DELETED_ROWS` bigint(4) DEFAULT NULL,
  `UPDATE_COUNT` bigint(4) DEFAULT NULL,
  `FREE_EXTENTS` bigint(4) DEFAULT NULL,
  `TOTAL_EXTENTS` bigint(4) DEFAULT NULL,
  `EXTENT_SIZE` bigint(4) NOT NULL DEFAULT '0',
  `INITIAL_SIZE` bigint(21) unsigned DEFAULT NULL,
  `MAXIMUM_SIZE` bigint(21) unsigned DEFAULT NULL,
  `AUTOEXTEND_SIZE` bigint(21) unsigned DEFAULT NULL,
  `CREATION_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  `LAST_ACCESS_TIME` datetime DEFAULT NULL,
  `RECOVER_TIME` bigint(4) DEFAULT NULL,
  `TRANSACTION_COUNTER` bigint(4) DEFAULT NULL,
  `VERSION` bigint(21) unsigned DEFAULT NULL,
  `ROW_FORMAT` varchar(10) DEFAULT NULL,
  `TABLE_ROWS` bigint(21) unsigned DEFAULT NULL,
  `AVG_ROW_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `MAX_DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `INDEX_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `DATA_FREE` bigint(21) unsigned DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CHECK_TIME` datetime DEFAULT NULL,
  `CHECKSUM` bigint(21) unsigned DEFAULT NULL,
  `STATUS` varchar(20) NOT NULL DEFAULT '',
  `EXTRA` varchar(255) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `FILES`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `GLOBAL_STATUS`
-- 

CREATE TEMPORARY TABLE `GLOBAL_STATUS` (
  `VARIABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `VARIABLE_VALUE` varchar(1024) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `GLOBAL_STATUS`
-- 

INSERT INTO `GLOBAL_STATUS` VALUES ('ABORTED_CLIENTS', '787');
INSERT INTO `GLOBAL_STATUS` VALUES ('ABORTED_CONNECTS', '39895');
INSERT INTO `GLOBAL_STATUS` VALUES ('BINLOG_CACHE_DISK_USE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('BINLOG_CACHE_USE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('BYTES_RECEIVED', '2888674156');
INSERT INTO `GLOBAL_STATUS` VALUES ('BYTES_SENT', '5129943396');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ADMIN_COMMANDS', '2072');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ASSIGN_TO_KEYCACHE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_DB', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_DB_UPGRADE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_EVENT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_FUNCTION', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_PROCEDURE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_SERVER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_TABLE', '56');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ALTER_TABLESPACE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ANALYZE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_BACKUP_TABLE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_BEGIN', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_BINLOG', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CALL_PROCEDURE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CHANGE_DB', '251430');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CHANGE_MASTER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CHECK', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CHECKSUM', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_COMMIT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_DB', '171');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_EVENT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_FUNCTION', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_INDEX', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_PROCEDURE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_SERVER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_TABLE', '373');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_TRIGGER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_UDF', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_USER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_CREATE_VIEW', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DEALLOC_SQL', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DELETE', '71');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DELETE_MULTI', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DO', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_DB', '10');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_EVENT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_FUNCTION', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_INDEX', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_PROCEDURE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_SERVER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_TABLE', '238');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_TRIGGER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_USER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_DROP_VIEW', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_EMPTY_QUERY', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_EXECUTE_SQL', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_FLUSH', '118');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_GRANT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_HA_CLOSE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_HA_OPEN', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_HA_READ', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_HELP', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_INSERT', '999');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_INSERT_SELECT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_INSTALL_PLUGIN', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_KILL', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_LOAD', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_LOAD_MASTER_DATA', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_LOAD_MASTER_TABLE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_LOCK_TABLES', '72869');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_OPTIMIZE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_PRELOAD_KEYS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_PREPARE_SQL', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_PURGE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_PURGE_BEFORE_DATE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_RELEASE_SAVEPOINT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_RENAME_TABLE', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_RENAME_USER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_REPAIR', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_REPLACE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_REPLACE_SELECT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_RESET', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_RESTORE_TABLE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_REVOKE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_REVOKE_ALL', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ROLLBACK', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_ROLLBACK_TO_SAVEPOINT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SAVEPOINT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SELECT', '26266280');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SET_OPTION', '989302');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_AUTHORS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_BINLOG_EVENTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_BINLOGS', '709');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CHARSETS', '3939');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_COLLATIONS', '3914');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_COLUMN_TYPES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CONTRIBUTORS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CREATE_DB', '44818');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CREATE_EVENT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CREATE_FUNC', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CREATE_PROC', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CREATE_TABLE', '157326');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_CREATE_TRIGGER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_DATABASES', '26068');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_ENGINE_LOGS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_ENGINE_MUTEX', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_ENGINE_STATUS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_EVENTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_ERRORS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_FIELDS', '157397');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_FUNCTION_STATUS', '44738');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_GRANTS', '1539');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_KEYS', '513');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_MASTER_STATUS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_NEW_MASTER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_OPEN_TABLES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_PLUGINS', '2');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_PRIVILEGES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_PROCEDURE_STATUS', '44738');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_PROCESSLIST', '24');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_PROFILE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_PROFILES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_SLAVE_HOSTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_SLAVE_STATUS', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_STATUS', '48011');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_STORAGE_ENGINES', '38');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_TABLE_STATUS', '158634');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_TABLES', '47398');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_TRIGGERS', '156748');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_VARIABLES', '56588');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SHOW_WARNINGS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SLAVE_START', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_SLAVE_STOP', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_CLOSE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_EXECUTE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_FETCH', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_PREPARE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_REPREPARE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_RESET', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_STMT_SEND_LONG_DATA', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_TRUNCATE', '3');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_UNINSTALL_PLUGIN', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_UNLOCK_TABLES', '89470');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_UPDATE', '75');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_UPDATE_MULTI', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_XA_COMMIT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_XA_END', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_XA_PREPARE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_XA_RECOVER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_XA_ROLLBACK', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COM_XA_START', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('COMPRESSION', 'OFF');
INSERT INTO `GLOBAL_STATUS` VALUES ('CONNECTIONS', '13052873');
INSERT INTO `GLOBAL_STATUS` VALUES ('CREATED_TMP_DISK_TABLES', '689392');
INSERT INTO `GLOBAL_STATUS` VALUES ('CREATED_TMP_FILES', '5');
INSERT INTO `GLOBAL_STATUS` VALUES ('CREATED_TMP_TABLES', '14640621');
INSERT INTO `GLOBAL_STATUS` VALUES ('DELAYED_ERRORS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('DELAYED_INSERT_THREADS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('DELAYED_WRITES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('FLUSH_COMMANDS', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_COMMIT', '38486');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_DELETE', '81');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_DISCOVER', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_PREPARE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_READ_FIRST', '169480');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_READ_KEY', '34042');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_READ_NEXT', '252167');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_READ_PREV', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_READ_RND', '240');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_READ_RND_NEXT', '103802742');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_ROLLBACK', '68');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_SAVEPOINT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_SAVEPOINT_ROLLBACK', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_UPDATE', '1748');
INSERT INTO `GLOBAL_STATUS` VALUES ('HANDLER_WRITE', '88466852');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_DATA', '511');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_DIRTY', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_FLUSHED', '3473');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_FREE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_MISC', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_TOTAL', '512');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_READ_AHEAD_RND', '418597');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_READ_AHEAD_SEQ', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_READ_REQUESTS', '363971055');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_READS', '13823032');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_WAIT_FREE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_BUFFER_POOL_WRITE_REQUESTS', '43485');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_FSYNCS', '2863');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_PENDING_FSYNCS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_PENDING_READS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_PENDING_WRITES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_READ', '3999698944');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_READS', '15107466');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_WRITES', '5438');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DATA_WRITTEN', '117460992');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DBLWR_PAGES_WRITTEN', '3473');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_DBLWR_WRITES', '478');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_LOG_WAITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_LOG_WRITE_REQUESTS', '5925');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_LOG_WRITES', '1544');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_OS_LOG_FSYNCS', '1907');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_OS_LOG_PENDING_FSYNCS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_OS_LOG_PENDING_WRITES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_OS_LOG_WRITTEN', '3471872');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_PAGE_SIZE', '16384');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_PAGES_CREATED', '203');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_PAGES_READ', '16496917');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_PAGES_WRITTEN', '3473');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROW_LOCK_CURRENT_WAITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROW_LOCK_TIME', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROW_LOCK_TIME_AVG', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROW_LOCK_TIME_MAX', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROW_LOCK_WAITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROWS_DELETED', '50');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROWS_INSERTED', '8045');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROWS_READ', '327099');
INSERT INTO `GLOBAL_STATUS` VALUES ('INNODB_ROWS_UPDATED', '6');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_BLOCKS_NOT_FLUSHED', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_BLOCKS_UNUSED', '7245');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_BLOCKS_USED', '28');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_READ_REQUESTS', '37767');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_READS', '16990');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_WRITE_REQUESTS', '633');
INSERT INTO `GLOBAL_STATUS` VALUES ('KEY_WRITES', '454');
INSERT INTO `GLOBAL_STATUS` VALUES ('LAST_QUERY_COST', '0.000000');
INSERT INTO `GLOBAL_STATUS` VALUES ('MAX_USED_CONNECTIONS', '25');
INSERT INTO `GLOBAL_STATUS` VALUES ('NOT_FLUSHED_DELAYED_ROWS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPEN_FILES', '100');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPEN_STREAMS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPEN_TABLE_DEFINITIONS', '256');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPEN_TABLES', '64');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPENED_FILES', '116316768');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPENED_TABLE_DEFINITIONS', '40527113');
INSERT INTO `GLOBAL_STATUS` VALUES ('OPENED_TABLES', '45446089');
INSERT INTO `GLOBAL_STATUS` VALUES ('PREPARED_STMT_COUNT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_FREE_BLOCKS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_FREE_MEMORY', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_HITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_INSERTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_LOWMEM_PRUNES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_NOT_CACHED', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_QUERIES_IN_CACHE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QCACHE_TOTAL_BLOCKS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('QUERIES', '41637097');
INSERT INTO `GLOBAL_STATUS` VALUES ('QUESTIONS', '41637097');
INSERT INTO `GLOBAL_STATUS` VALUES ('RPL_STATUS', 'NULL');
INSERT INTO `GLOBAL_STATUS` VALUES ('SELECT_FULL_JOIN', '71');
INSERT INTO `GLOBAL_STATUS` VALUES ('SELECT_FULL_RANGE_JOIN', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SELECT_RANGE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SELECT_RANGE_CHECK', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SELECT_SCAN', '13854184');
INSERT INTO `GLOBAL_STATUS` VALUES ('SLAVE_OPEN_TEMP_TABLES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SLAVE_RETRIED_TRANSACTIONS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SLAVE_RUNNING', 'OFF');
INSERT INTO `GLOBAL_STATUS` VALUES ('SLOW_LAUNCH_THREADS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SLOW_QUERIES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SORT_MERGE_PASSES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SORT_RANGE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SORT_ROWS', '250');
INSERT INTO `GLOBAL_STATUS` VALUES ('SORT_SCAN', '89741');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_ACCEPT_RENEGOTIATES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_ACCEPTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CALLBACK_CACHE_HITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CIPHER', '');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CIPHER_LIST', '');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CLIENT_CONNECTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CONNECT_RENEGOTIATES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CTX_VERIFY_DEPTH', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_CTX_VERIFY_MODE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_DEFAULT_TIMEOUT', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_FINISHED_ACCEPTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_FINISHED_CONNECTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSION_CACHE_HITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSION_CACHE_MISSES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSION_CACHE_MODE', 'NONE');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSION_CACHE_OVERFLOWS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSION_CACHE_SIZE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSION_CACHE_TIMEOUTS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_SESSIONS_REUSED', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_USED_SESSION_CACHE_ENTRIES', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_VERIFY_DEPTH', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_VERIFY_MODE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('SSL_VERSION', '');
INSERT INTO `GLOBAL_STATUS` VALUES ('TABLE_LOCKS_IMMEDIATE', '295189');
INSERT INTO `GLOBAL_STATUS` VALUES ('TABLE_LOCKS_WAITED', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('TC_LOG_MAX_PAGES_USED', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('TC_LOG_PAGE_SIZE', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('TC_LOG_PAGE_WAITS', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('THREADS_CACHED', '0');
INSERT INTO `GLOBAL_STATUS` VALUES ('THREADS_CONNECTED', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('THREADS_CREATED', '13052872');
INSERT INTO `GLOBAL_STATUS` VALUES ('THREADS_RUNNING', '1');
INSERT INTO `GLOBAL_STATUS` VALUES ('UPTIME', '7162861');
INSERT INTO `GLOBAL_STATUS` VALUES ('UPTIME_SINCE_FLUSH_STATUS', '7162861');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `GLOBAL_VARIABLES`
-- 

CREATE TEMPORARY TABLE `GLOBAL_VARIABLES` (
  `VARIABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `VARIABLE_VALUE` varchar(1024) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `GLOBAL_VARIABLES`
-- 

INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_PREPARED_STMT_COUNT', '16382');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SETS_DIR', '/usr/share/mysql/charsets/');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_CRYPT', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CONNECT_TIMEOUT', '10');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_REPAIR_THREADS', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('AUTOMATIC_SP_PRIVILEGES', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_CONNECT_ERRORS', '10');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BINLOG_CACHE_SIZE', '32768');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_CONCURRENCY_TICKETS', '500');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BACK_LOG', '50');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_CONNECTIONS', '151');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('STORAGE_ENGINE', 'MyISAM');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DELAYED_INSERT_TIMEOUT', '300');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_LENGTH_FOR_SORT_DATA', '1024');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_BIN_TRUST_ROUTINE_CREATORS', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_CONNECTION', 'latin1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SSL_CIPHER', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_RESULTS', 'latin1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BASEDIR', '/usr/');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LARGE_PAGES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('UPDATABLE_VIEWS_WITH_LIMIT', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TABLE_DEFINITION_CACHE', '256');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLOW_LAUNCH_TIME', '2');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_ALLOC_BLOCK_SIZE', '8192');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_LOCK_WAIT_TIMEOUT', '50');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('COMPLETION_TYPE', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RELAY_LOG_INDEX', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('COLLATION_SERVER', 'latin1_swedish_ci');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_QUOTE_SHOW_CREATE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('EVENT_SCHEDULER', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_LOG_UPDATE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('COLLATION_DATABASE', 'latin1_swedish_ci');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_USE_LEGACY_CARDINALITY_ALGORITHM', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FT_MAX_WORD_LEN', '84');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOCKED_IN_MEMORY', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CONCURRENT_INSERT', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_RECOVER_OPTIONS', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('NET_WRITE_TIMEOUT', '60');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('REPORT_HOST', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('AUTO_INCREMENT_OFFSET', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SSL_KEY', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FLUSH', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_DATABASE', 'latin1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DELAYED_INSERT_LIMIT', '100');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_SKIP_ERRORS', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FT_QUERY_EXPANSION_LIMIT', '20');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INSERT_ID', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_LOW_PRIORITY_UPDATES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RELAY_LOG_PURGE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LANGUAGE', '/usr/share/mysql/english/');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SKIP_SHOW_DATABASE', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('KEY_CACHE_AGE_THRESHOLD', '300');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('GROUP_CONCAT_MAX_LEN', '1024');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('JOIN_BUFFER_SIZE', '131072');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_LOG_BUFFER_SIZE', '1048576');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('VERSION_COMPILE_MACHINE', 'i386');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('READ_BUFFER_SIZE', '131072');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_MAX_PURGE_LAG', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DELAYED_QUEUE_SIZE', '1000');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TRANSACTION_PREALLOC_SIZE', '4096');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INTERACTIVE_TIMEOUT', '28800');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('VERSION_COMPILE_OS', 'redhat-linux-gnu');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('WAIT_TIMEOUT', '28800');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_NOTES', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TABLE_OPEN_CACHE', '64');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOW_PRIORITY_UPDATES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LARGE_PAGE_SIZE', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('REPORT_PASSWORD', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INIT_SLAVE', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_INSERT_DELAYED_THREADS', '20');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_BINLOG_SIZE', '1073741824');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_COMPRESS', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_ERROR_COUNT', '64');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TRANSACTION_ALLOC_BLOCK_SIZE', '8192');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FT_MIN_WORD_LEN', '4');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('THREAD_CACHE_SIZE', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_WARNINGS', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_DOUBLEWRITE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_LOG_OFF', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PSEUDO_THREAD_ID', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_LONG_DATA_SIZE', '1048576');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DEFAULT_WEEK_FORMAT', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_FLUSH_METHOD', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_OUTPUT', 'FILE');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOWER_CASE_TABLE_NAMES', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_RELAY_LOG_SIZE', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_STATS_METHOD', 'nulls_unequal');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PROTOCOL_VERSION', '10');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('NET_RETRY_COUNT', '10');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_SYMLINK', 'DISABLED');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_SORT_LENGTH', '1024');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TIME_ZONE', 'SYSTEM');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_HEAP_TABLE_SIZE', '16777216');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_TABLE_LOCKS', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FT_BOOLEAN_SYNTAX', '+ -><()~*:""&|');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_AUTOEXTEND_INCREMENT', '8');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_QUERIES_NOT_USING_INDEXES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_DATA_POINTER_SIZE', '6');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('NET_BUFFER_LENGTH', '16384');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_THREAD_SLEEP_DELAY', '10000');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FT_STOPWORD_FILE', '(built-in)');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_AUTO_IS_NULL', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DELAY_KEY_WRITE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_TRANSACTION_RETRIES', '10');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MULTI_RANGE_COUNT', '256');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_ERROR', '/var/log/mysqld.log');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LONG_QUERY_TIME', '10.000000');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('GENERAL_LOG', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BULK_INSERT_BUFFER_SIZE', '8388608');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TABLE_TYPE', 'MyISAM');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_MAX_ALLOWED_PACKET', '1073741824');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_CACHE_MIN_RES_UNIT', '4096');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_USE_MMAP', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_STATS_ON_METADATA', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_BINLOG_CACHE_SIZE', '4294963200');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_ROLLBACK_ON_TIMEOUT', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_CACHE_TYPE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PROFILING_HISTORY_SIZE', '15');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_FILESYSTEM', 'binary');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_ADDITIONAL_MEM_POOL_SIZE', '1048576');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TX_ISOLATION', 'REPEATABLE-READ');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('READ_RND_BUFFER_SIZE', '262144');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('THREAD_HANDLING', 'one-thread-per-connection');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SECURE_AUTH', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BINLOG_DIRECT_NON_TRANSACTIONAL_UPDATES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BIG_TABLES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FLUSH_TIME', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_SELECT_LIMIT', '18446744073709551615');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_INNODB', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DATE_FORMAT', '%Y-%m-%d');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OLD_PASSWORDS', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('READ_ONLY', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_PREALLOC_SIZE', '8192');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RAND_SEED1', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLOW_QUERY_LOG_FILE', '/var/run/mysqld/mysqld-slow.log');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_DATA_FILE_PATH', 'ibdata1:10M:autoextend');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_SEEKS_FOR_KEY', '4294967295');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_CACHE_LIMIT', '1048576');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SYSTEM_TIME_ZONE', 'CEST');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PROFILING', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SSL_CERT', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_CACHE_SIZE', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_MAX_SORT_FILE_SIZE', '2146435072');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('KEY_CACHE_DIVISION_LIMIT', '100');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('THREAD_STACK', '196608');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('GENERAL_LOG_FILE', '/var/run/mysqld/mysqld.log');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OPEN_FILES_LIMIT', '1024');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_SORT_BUFFER_SIZE', '8388608');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SKIP_NAME_RESOLVE', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('AUTO_INCREMENT_INCREMENT', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LAST_INSERT_ID', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TMPDIR', '/tmp');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PID_FILE', '/var/run/mysqld/mysqld.pid');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('EXPIRE_LOGS_DAYS', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_TMP_TABLES', '32');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_PARTITIONING', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MIN_EXAMINED_ROW_LIMIT', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('FOREIGN_KEY_CHECKS', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LARGE_FILES_SUPPORT', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RELAY_LOG_INFO_FILE', 'relay-log.info');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SSL_CAPATH', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_AUTOINC_LOCK_MODE', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('VERSION_COMMENT', 'Source distribution');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_COMMIT_CONCURRENCY', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OPTIMIZER_SWITCH', 'index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_MIRRORED_LOG_GROUPS', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OPTIMIZER_PRUNE_LEVEL', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('UNIQUE_CHECKS', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('QUERY_CACHE_WLOCK_INVALIDATE', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('VERSION', '5.1.73');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_WRITE_LOCK_COUNT', '4294967295');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_SUPPORT_XA', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TIMED_MUTEXES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_SYNC_SPIN_LOOPS', '20');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INIT_FILE', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('COLLATION_CONNECTION', 'latin1_swedish_ci');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LC_TIME_NAMES', 'en_US');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_QUERY_CACHE', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SERVER_ID', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_ADAPTIVE_HASH_INDEX', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SKIP_NETWORKING', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RPL_RECOVERY_RANK', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_SYSTEM', 'utf8');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DIV_PRECISION_INCREMENT', '4');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INIT_CONNECT', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DATADIR', '/var/lib/mysql/');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OPTIMIZER_SEARCH_DEPTH', '62');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_DATA_HOME_DIR', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('AUTOCOMMIT', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SKIP_EXTERNAL_LOCKING', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('KEY_CACHE_BLOCK_SIZE', '1024');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_SLAVE_SKIP_COUNTER', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_BIG_TABLES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TIME_FORMAT', '%H:%i:%s');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TMP_TABLE_SIZE', '16777216');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_FORCE_RECOVERY', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TABLE_LOCK_WAIT_TIMEOUT', '50');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_LOG_FILES_IN_GROUP', '2');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_SERVER', 'latin1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('ERROR_COUNT', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HOSTNAME', 'oraclepr.priv.uco.es');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_DYNAMIC_LOADING', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_BUFFER_RESULT', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SYNC_BINLOG', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_BIN', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RELAY_LOG_SPACE_LIMIT', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_MODE', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_OPEN_FILES', '300');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_USER_CONNECTIONS', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OLD', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_EXEC_MODE', 'STRICT');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_MAX_DIRTY_PAGES_PCT', '90');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_MAX_JOIN_SIZE', '18446744073709551615');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_COMMUNITY_FEATURES', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('NEW', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('KEY_BUFFER_SIZE', '8384512');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SECURE_FILE_PRIV', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_NDBCLUSTER', 'NO');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('KEEP_FILES_ON_CREATE', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('REPORT_PORT', '3306');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('REPORT_USER', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_STATS_METHOD', 'nulls_equal');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('ENGINE_CONDITION_PUSHDOWN', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_FAST_SHUTDOWN', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_LOG_BIN', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SSL_CA', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOWER_CASE_FILE_SYSTEM', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_THREAD_CONCURRENCY', '8');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_CSV', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_NET_TIMEOUT', '3600');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_SAFE_UPDATES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_BUFFER_POOL_SIZE', '8388608');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PRELOAD_BUFFER_SIZE', '32768');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOCAL_INFILE', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_COMPRESSED_PROTOCOL', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_RTREE_KEYS', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_GEOMETRY', 'YES');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('BINLOG_FORMAT', 'STATEMENT');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_SP_RECURSION_DEPTH', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('OLD_ALTER_TABLE', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RELAY_LOG', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MYISAM_MMAP_SIZE', '4294967295');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PLUGIN_DIR', '/usr/lib/mysql/plugin');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_SLOW_QUERIES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('IGNORE_BUILTIN_INNODB', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('TIMESTAMP', '1760263577');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('NET_READ_TIMEOUT', '30');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_DELAYED_THREADS', '20');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SYNC_FRM', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('DATETIME_FORMAT', '%Y-%m-%d %H:%i:%s');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLOW_QUERY_LOG', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_FLUSH_LOG_AT_TRX_COMMIT', '1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('WARNING_COUNT', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_FILE_IO_THREADS', '4');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_ALLOWED_PACKET', '1048576');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LICENSE', 'GPL');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_BIG_SELECTS', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RANGE_ALLOC_BLOCK_SIZE', '4096');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_CHECKSUMS', 'ON');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('PORT', '3306');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_LOCKS_UNSAFE_FOR_BINLOG', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_SSL', 'DISABLED');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_LOG_FILE_SIZE', '5242880');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_BIN_TRUST_FUNCTION_CREATORS', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('LOG_SLAVE_UPDATES', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('MAX_JOIN_SIZE', '18446744073709551615');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SORT_BUFFER_SIZE', '2097144');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('HAVE_OPENSSL', 'DISABLED');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_FILE_PER_TABLE', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('INNODB_LOG_GROUP_HOME_DIR', './');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SOCKET', '/var/lib/mysql/mysql.sock');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('CHARACTER_SET_CLIENT', 'latin1');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('RAND_SEED2', '');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('IDENTITY', '0');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SQL_WARNINGS', 'OFF');
INSERT INTO `GLOBAL_VARIABLES` VALUES ('SLAVE_LOAD_TMPDIR', '/tmp');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `KEY_COLUMN_USAGE`
-- 

CREATE TEMPORARY TABLE `KEY_COLUMN_USAGE` (
  `CONSTRAINT_CATALOG` varchar(512) DEFAULT NULL,
  `CONSTRAINT_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `CONSTRAINT_NAME` varchar(64) NOT NULL DEFAULT '',
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `COLUMN_NAME` varchar(64) NOT NULL DEFAULT '',
  `ORDINAL_POSITION` bigint(10) NOT NULL DEFAULT '0',
  `POSITION_IN_UNIQUE_CONSTRAINT` bigint(10) DEFAULT NULL,
  `REFERENCED_TABLE_SCHEMA` varchar(64) DEFAULT NULL,
  `REFERENCED_TABLE_NAME` varchar(64) DEFAULT NULL,
  `REFERENCED_COLUMN_NAME` varchar(64) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `KEY_COLUMN_USAGE`
-- 

INSERT INTO `KEY_COLUMN_USAGE` VALUES (NULL, 'i32esrah', 'PRIMARY', NULL, 'i32esrah', 'socios', 'id', 1, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `PARTITIONS`
-- 

CREATE TEMPORARY TABLE `PARTITIONS` (
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `PARTITION_NAME` varchar(64) DEFAULT NULL,
  `SUBPARTITION_NAME` varchar(64) DEFAULT NULL,
  `PARTITION_ORDINAL_POSITION` bigint(21) unsigned DEFAULT NULL,
  `SUBPARTITION_ORDINAL_POSITION` bigint(21) unsigned DEFAULT NULL,
  `PARTITION_METHOD` varchar(12) DEFAULT NULL,
  `SUBPARTITION_METHOD` varchar(12) DEFAULT NULL,
  `PARTITION_EXPRESSION` longtext,
  `SUBPARTITION_EXPRESSION` longtext,
  `PARTITION_DESCRIPTION` longtext,
  `TABLE_ROWS` bigint(21) unsigned NOT NULL DEFAULT '0',
  `AVG_ROW_LENGTH` bigint(21) unsigned NOT NULL DEFAULT '0',
  `DATA_LENGTH` bigint(21) unsigned NOT NULL DEFAULT '0',
  `MAX_DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `INDEX_LENGTH` bigint(21) unsigned NOT NULL DEFAULT '0',
  `DATA_FREE` bigint(21) unsigned NOT NULL DEFAULT '0',
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CHECK_TIME` datetime DEFAULT NULL,
  `CHECKSUM` bigint(21) unsigned DEFAULT NULL,
  `PARTITION_COMMENT` varchar(80) NOT NULL DEFAULT '',
  `NODEGROUP` varchar(12) NOT NULL DEFAULT '',
  `TABLESPACE_NAME` varchar(64) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `PARTITIONS`
-- 

INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'CHARACTER_SETS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 384, 0, 16604160, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'COLLATIONS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 231, 0, 16704765, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'COLLATION_CHARACTER_SET_APPLICABILITY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 195, 0, 16691610, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'COLUMNS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 2565, 0, 16757145, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'ENGINES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 490, 0, 16709000, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'EVENTS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'FILES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 2677, 0, 16758020, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'GLOBAL_STATUS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 3268, 0, 16755036, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'GLOBAL_VARIABLES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 3268, 0, 16755036, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 4637, 0, 16762755, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'PARTITIONS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'PLUGINS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'PROCESSLIST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'PROFILING', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 308, 0, 16562084, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 4814, 0, 16767162, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'ROUTINES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'SCHEMATA', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 3464, 0, 16755368, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 2179, 0, 16767405, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'SESSION_STATUS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 3268, 0, 16755036, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'SESSION_VARIABLES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 3268, 0, 16755036, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'STATISTICS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 2679, 0, 16770540, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'TABLES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 3545, 0, 16760760, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 2504, 0, 16749256, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 2372, 0, 16748692, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'TRIGGERS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'USER_PRIVILEGES', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 1986, 0, 16759854, 0, 0, '2025-10-12 12:06:17', NULL, NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'information_schema', 'VIEWS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 281474976710655, 1024, 0, '2025-10-12 12:06:17', '2025-10-12 12:06:17', NULL, NULL, '', '', NULL);
INSERT INTO `PARTITIONS` VALUES (NULL, 'i32esrah', 'socios', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 5461, 16384, NULL, 0, 1435500544, '2025-10-11 15:28:45', NULL, NULL, NULL, '', '', NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `PLUGINS`
-- 

CREATE TEMPORARY TABLE `PLUGINS` (
  `PLUGIN_NAME` varchar(64) NOT NULL DEFAULT '',
  `PLUGIN_VERSION` varchar(20) NOT NULL DEFAULT '',
  `PLUGIN_STATUS` varchar(10) NOT NULL DEFAULT '',
  `PLUGIN_TYPE` varchar(80) NOT NULL DEFAULT '',
  `PLUGIN_TYPE_VERSION` varchar(20) NOT NULL DEFAULT '',
  `PLUGIN_LIBRARY` varchar(64) DEFAULT NULL,
  `PLUGIN_LIBRARY_VERSION` varchar(20) DEFAULT NULL,
  `PLUGIN_AUTHOR` varchar(64) DEFAULT NULL,
  `PLUGIN_DESCRIPTION` longtext,
  `PLUGIN_LICENSE` varchar(80) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `PLUGINS`
-- 

INSERT INTO `PLUGINS` VALUES ('binlog', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'MySQL AB', 'This is a pseudo storage engine to represent the binlog in a transaction', 'GPL');
INSERT INTO `PLUGINS` VALUES ('partition', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'Mikael Ronstrom, MySQL AB', 'Partition Storage Engine Helper', 'GPL');
INSERT INTO `PLUGINS` VALUES ('CSV', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'Brian Aker, MySQL AB', 'CSV storage engine', 'GPL');
INSERT INTO `PLUGINS` VALUES ('MEMORY', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'MySQL AB', 'Hash based, stored in memory, useful for temporary tables', 'GPL');
INSERT INTO `PLUGINS` VALUES ('InnoDB', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'Innobase OY', 'Supports transactions, row-level locking, and foreign keys', 'GPL');
INSERT INTO `PLUGINS` VALUES ('MyISAM', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'MySQL AB', 'Default engine as of MySQL 3.23 with great performance', 'GPL');
INSERT INTO `PLUGINS` VALUES ('MRG_MYISAM', '1.0', 'ACTIVE', 'STORAGE ENGINE', '50173.0', NULL, NULL, 'MySQL AB', 'Collection of identical MyISAM tables', 'GPL');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `PROCESSLIST`
-- 

CREATE TEMPORARY TABLE `PROCESSLIST` (
  `ID` bigint(4) NOT NULL DEFAULT '0',
  `USER` varchar(16) NOT NULL DEFAULT '',
  `HOST` varchar(64) NOT NULL DEFAULT '',
  `DB` varchar(64) DEFAULT NULL,
  `COMMAND` varchar(16) NOT NULL DEFAULT '',
  `TIME` int(7) NOT NULL DEFAULT '0',
  `STATE` varchar(64) DEFAULT NULL,
  `INFO` longtext
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `PROCESSLIST`
-- 

INSERT INTO `PROCESSLIST` VALUES (13052872, 'i32esrah', 'oraclepr.uco.es:15855', NULL, 'Query', 0, 'executing', 'SELECT * FROM `information_schema`.`PROCESSLIST`');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `PROFILING`
-- 

CREATE TEMPORARY TABLE `PROFILING` (
  `QUERY_ID` int(20) NOT NULL DEFAULT '0',
  `SEQ` int(20) NOT NULL DEFAULT '0',
  `STATE` varchar(30) NOT NULL DEFAULT '',
  `DURATION` decimal(9,6) NOT NULL DEFAULT '0.000000',
  `CPU_USER` decimal(9,6) DEFAULT NULL,
  `CPU_SYSTEM` decimal(9,6) DEFAULT NULL,
  `CONTEXT_VOLUNTARY` int(20) DEFAULT NULL,
  `CONTEXT_INVOLUNTARY` int(20) DEFAULT NULL,
  `BLOCK_OPS_IN` int(20) DEFAULT NULL,
  `BLOCK_OPS_OUT` int(20) DEFAULT NULL,
  `MESSAGES_SENT` int(20) DEFAULT NULL,
  `MESSAGES_RECEIVED` int(20) DEFAULT NULL,
  `PAGE_FAULTS_MAJOR` int(20) DEFAULT NULL,
  `PAGE_FAULTS_MINOR` int(20) DEFAULT NULL,
  `SWAPS` int(20) DEFAULT NULL,
  `SOURCE_FUNCTION` varchar(30) DEFAULT NULL,
  `SOURCE_FILE` varchar(20) DEFAULT NULL,
  `SOURCE_LINE` int(20) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `PROFILING`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `REFERENTIAL_CONSTRAINTS`
-- 

CREATE TEMPORARY TABLE `REFERENTIAL_CONSTRAINTS` (
  `CONSTRAINT_CATALOG` varchar(512) DEFAULT NULL,
  `CONSTRAINT_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `CONSTRAINT_NAME` varchar(64) NOT NULL DEFAULT '',
  `UNIQUE_CONSTRAINT_CATALOG` varchar(512) DEFAULT NULL,
  `UNIQUE_CONSTRAINT_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `UNIQUE_CONSTRAINT_NAME` varchar(64) DEFAULT NULL,
  `MATCH_OPTION` varchar(64) NOT NULL DEFAULT '',
  `UPDATE_RULE` varchar(64) NOT NULL DEFAULT '',
  `DELETE_RULE` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `REFERENCED_TABLE_NAME` varchar(64) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `REFERENTIAL_CONSTRAINTS`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `ROUTINES`
-- 

CREATE TEMPORARY TABLE `ROUTINES` (
  `SPECIFIC_NAME` varchar(64) NOT NULL DEFAULT '',
  `ROUTINE_CATALOG` varchar(512) DEFAULT NULL,
  `ROUTINE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `ROUTINE_NAME` varchar(64) NOT NULL DEFAULT '',
  `ROUTINE_TYPE` varchar(9) NOT NULL DEFAULT '',
  `DTD_IDENTIFIER` varchar(64) DEFAULT NULL,
  `ROUTINE_BODY` varchar(8) NOT NULL DEFAULT '',
  `ROUTINE_DEFINITION` longtext,
  `EXTERNAL_NAME` varchar(64) DEFAULT NULL,
  `EXTERNAL_LANGUAGE` varchar(64) DEFAULT NULL,
  `PARAMETER_STYLE` varchar(8) NOT NULL DEFAULT '',
  `IS_DETERMINISTIC` varchar(3) NOT NULL DEFAULT '',
  `SQL_DATA_ACCESS` varchar(64) NOT NULL DEFAULT '',
  `SQL_PATH` varchar(64) DEFAULT NULL,
  `SECURITY_TYPE` varchar(7) NOT NULL DEFAULT '',
  `CREATED` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LAST_ALTERED` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SQL_MODE` varchar(8192) NOT NULL DEFAULT '',
  `ROUTINE_COMMENT` varchar(64) NOT NULL DEFAULT '',
  `DEFINER` varchar(77) NOT NULL DEFAULT '',
  `CHARACTER_SET_CLIENT` varchar(32) NOT NULL DEFAULT '',
  `COLLATION_CONNECTION` varchar(32) NOT NULL DEFAULT '',
  `DATABASE_COLLATION` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `ROUTINES`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `SCHEMATA`
-- 

CREATE TEMPORARY TABLE `SCHEMATA` (
  `CATALOG_NAME` varchar(512) DEFAULT NULL,
  `SCHEMA_NAME` varchar(64) NOT NULL DEFAULT '',
  `DEFAULT_CHARACTER_SET_NAME` varchar(32) NOT NULL DEFAULT '',
  `DEFAULT_COLLATION_NAME` varchar(32) NOT NULL DEFAULT '',
  `SQL_PATH` varchar(512) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `SCHEMATA`
-- 

INSERT INTO `SCHEMATA` VALUES (NULL, 'information_schema', 'utf8', 'utf8_general_ci', NULL);
INSERT INTO `SCHEMATA` VALUES (NULL, 'i32esrah', 'latin1', 'latin1_swedish_ci', NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `SCHEMA_PRIVILEGES`
-- 

CREATE TEMPORARY TABLE `SCHEMA_PRIVILEGES` (
  `GRANTEE` varchar(81) NOT NULL DEFAULT '',
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `PRIVILEGE_TYPE` varchar(64) NOT NULL DEFAULT '',
  `IS_GRANTABLE` varchar(3) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `SCHEMA_PRIVILEGES`
-- 

INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'SELECT', 'NO');
INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'INSERT', 'NO');
INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'UPDATE', 'NO');
INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'DELETE', 'NO');
INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'CREATE', 'NO');
INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'DROP', 'NO');
INSERT INTO `SCHEMA_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'i32esrah', 'ALTER', 'NO');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `SESSION_STATUS`
-- 

CREATE TEMPORARY TABLE `SESSION_STATUS` (
  `VARIABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `VARIABLE_VALUE` varchar(1024) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `SESSION_STATUS`
-- 

INSERT INTO `SESSION_STATUS` VALUES ('ABORTED_CLIENTS', '787');
INSERT INTO `SESSION_STATUS` VALUES ('ABORTED_CONNECTS', '39895');
INSERT INTO `SESSION_STATUS` VALUES ('BINLOG_CACHE_DISK_USE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('BINLOG_CACHE_USE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('BYTES_RECEIVED', '5099');
INSERT INTO `SESSION_STATUS` VALUES ('BYTES_SENT', '127263');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ADMIN_COMMANDS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ASSIGN_TO_KEYCACHE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_DB', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_DB_UPGRADE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_EVENT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_FUNCTION', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_PROCEDURE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_SERVER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ALTER_TABLESPACE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ANALYZE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_BACKUP_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_BEGIN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_BINLOG', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CALL_PROCEDURE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CHANGE_DB', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CHANGE_MASTER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CHECK', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CHECKSUM', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_COMMIT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_DB', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_EVENT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_FUNCTION', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_INDEX', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_PROCEDURE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_SERVER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_TRIGGER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_UDF', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_USER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_CREATE_VIEW', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DEALLOC_SQL', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DELETE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DELETE_MULTI', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DO', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_DB', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_EVENT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_FUNCTION', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_INDEX', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_PROCEDURE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_SERVER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_TRIGGER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_USER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_DROP_VIEW', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_EMPTY_QUERY', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_EXECUTE_SQL', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_FLUSH', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_GRANT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_HA_CLOSE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_HA_OPEN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_HA_READ', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_HELP', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_INSERT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_INSERT_SELECT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_INSTALL_PLUGIN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_KILL', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_LOAD', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_LOAD_MASTER_DATA', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_LOAD_MASTER_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_LOCK_TABLES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_OPTIMIZE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_PRELOAD_KEYS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_PREPARE_SQL', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_PURGE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_PURGE_BEFORE_DATE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_RELEASE_SAVEPOINT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_RENAME_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_RENAME_USER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_REPAIR', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_REPLACE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_REPLACE_SELECT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_RESET', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_RESTORE_TABLE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_REVOKE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_REVOKE_ALL', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ROLLBACK', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_ROLLBACK_TO_SAVEPOINT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SAVEPOINT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SELECT', '23');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SET_OPTION', '26');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_AUTHORS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_BINLOG_EVENTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_BINLOGS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CHARSETS', '1');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_COLLATIONS', '1');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_COLUMN_TYPES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CONTRIBUTORS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CREATE_DB', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CREATE_EVENT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CREATE_FUNC', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CREATE_PROC', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CREATE_TABLE', '21');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_CREATE_TRIGGER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_DATABASES', '1');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_ENGINE_LOGS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_ENGINE_MUTEX', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_ENGINE_STATUS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_EVENTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_ERRORS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_FIELDS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_FUNCTION_STATUS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_GRANTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_KEYS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_MASTER_STATUS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_NEW_MASTER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_OPEN_TABLES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_PLUGINS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_PRIVILEGES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_PROCEDURE_STATUS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_PROCESSLIST', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_PROFILE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_PROFILES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_SLAVE_HOSTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_SLAVE_STATUS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_STATUS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_STORAGE_ENGINES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_TABLE_STATUS', '21');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_TABLES', '2');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_TRIGGERS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_VARIABLES', '2');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SHOW_WARNINGS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SLAVE_START', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_SLAVE_STOP', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_CLOSE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_EXECUTE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_FETCH', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_PREPARE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_REPREPARE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_RESET', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_STMT_SEND_LONG_DATA', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_TRUNCATE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_UNINSTALL_PLUGIN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_UNLOCK_TABLES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_UPDATE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_UPDATE_MULTI', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_XA_COMMIT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_XA_END', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_XA_PREPARE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_XA_RECOVER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_XA_ROLLBACK', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COM_XA_START', '0');
INSERT INTO `SESSION_STATUS` VALUES ('COMPRESSION', 'OFF');
INSERT INTO `SESSION_STATUS` VALUES ('CONNECTIONS', '13052873');
INSERT INTO `SESSION_STATUS` VALUES ('CREATED_TMP_DISK_TABLES', '50');
INSERT INTO `SESSION_STATUS` VALUES ('CREATED_TMP_FILES', '5');
INSERT INTO `SESSION_STATUS` VALUES ('CREATED_TMP_TABLES', '201');
INSERT INTO `SESSION_STATUS` VALUES ('DELAYED_ERRORS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('DELAYED_INSERT_THREADS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('DELAYED_WRITES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('FLUSH_COMMANDS', '1');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_COMMIT', '1');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_DELETE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_DISCOVER', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_PREPARE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_READ_FIRST', '2');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_READ_KEY', '1');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_READ_NEXT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_READ_PREV', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_READ_RND', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_READ_RND_NEXT', '1511');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_ROLLBACK', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_SAVEPOINT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_SAVEPOINT_ROLLBACK', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_UPDATE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('HANDLER_WRITE', '1630');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_DATA', '511');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_DIRTY', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_FLUSHED', '3473');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_FREE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_MISC', '1');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_PAGES_TOTAL', '512');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_READ_AHEAD_RND', '418597');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_READ_AHEAD_SEQ', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_READ_REQUESTS', '363971076');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_READS', '13823032');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_WAIT_FREE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_BUFFER_POOL_WRITE_REQUESTS', '43485');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_FSYNCS', '2863');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_PENDING_FSYNCS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_PENDING_READS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_PENDING_WRITES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_READ', '3999698944');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_READS', '15107466');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_WRITES', '5438');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DATA_WRITTEN', '117460992');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DBLWR_PAGES_WRITTEN', '3473');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_DBLWR_WRITES', '478');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_LOG_WAITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_LOG_WRITE_REQUESTS', '5925');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_LOG_WRITES', '1544');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_OS_LOG_FSYNCS', '1907');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_OS_LOG_PENDING_FSYNCS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_OS_LOG_PENDING_WRITES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_OS_LOG_WRITTEN', '3471872');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_PAGE_SIZE', '16384');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_PAGES_CREATED', '203');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_PAGES_READ', '16496917');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_PAGES_WRITTEN', '3473');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROW_LOCK_CURRENT_WAITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROW_LOCK_TIME', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROW_LOCK_TIME_AVG', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROW_LOCK_TIME_MAX', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROW_LOCK_WAITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROWS_DELETED', '50');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROWS_INSERTED', '8045');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROWS_READ', '327099');
INSERT INTO `SESSION_STATUS` VALUES ('INNODB_ROWS_UPDATED', '6');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_BLOCKS_NOT_FLUSHED', '0');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_BLOCKS_UNUSED', '7245');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_BLOCKS_USED', '28');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_READ_REQUESTS', '37767');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_READS', '16990');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_WRITE_REQUESTS', '633');
INSERT INTO `SESSION_STATUS` VALUES ('KEY_WRITES', '454');
INSERT INTO `SESSION_STATUS` VALUES ('LAST_QUERY_COST', '10.499000');
INSERT INTO `SESSION_STATUS` VALUES ('MAX_USED_CONNECTIONS', '25');
INSERT INTO `SESSION_STATUS` VALUES ('NOT_FLUSHED_DELAYED_ROWS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('OPEN_FILES', '102');
INSERT INTO `SESSION_STATUS` VALUES ('OPEN_STREAMS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('OPEN_TABLE_DEFINITIONS', '256');
INSERT INTO `SESSION_STATUS` VALUES ('OPEN_TABLES', '64');
INSERT INTO `SESSION_STATUS` VALUES ('OPENED_FILES', '116316915');
INSERT INTO `SESSION_STATUS` VALUES ('OPENED_TABLE_DEFINITIONS', '2');
INSERT INTO `SESSION_STATUS` VALUES ('OPENED_TABLES', '3');
INSERT INTO `SESSION_STATUS` VALUES ('PREPARED_STMT_COUNT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_FREE_BLOCKS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_FREE_MEMORY', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_HITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_INSERTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_LOWMEM_PRUNES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_NOT_CACHED', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_QUERIES_IN_CACHE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QCACHE_TOTAL_BLOCKS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('QUERIES', '41637141');
INSERT INTO `SESSION_STATUS` VALUES ('QUESTIONS', '98');
INSERT INTO `SESSION_STATUS` VALUES ('RPL_STATUS', 'NULL');
INSERT INTO `SESSION_STATUS` VALUES ('SELECT_FULL_JOIN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SELECT_FULL_RANGE_JOIN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SELECT_RANGE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SELECT_RANGE_CHECK', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SELECT_SCAN', '50');
INSERT INTO `SESSION_STATUS` VALUES ('SLAVE_OPEN_TEMP_TABLES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SLAVE_RETRIED_TRANSACTIONS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SLAVE_RUNNING', 'OFF');
INSERT INTO `SESSION_STATUS` VALUES ('SLOW_LAUNCH_THREADS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SLOW_QUERIES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SORT_MERGE_PASSES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SORT_RANGE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SORT_ROWS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SORT_SCAN', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_ACCEPT_RENEGOTIATES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_ACCEPTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CALLBACK_CACHE_HITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CIPHER', '');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CIPHER_LIST', '');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CLIENT_CONNECTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CONNECT_RENEGOTIATES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CTX_VERIFY_DEPTH', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_CTX_VERIFY_MODE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_DEFAULT_TIMEOUT', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_FINISHED_ACCEPTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_FINISHED_CONNECTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSION_CACHE_HITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSION_CACHE_MISSES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSION_CACHE_MODE', 'NONE');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSION_CACHE_OVERFLOWS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSION_CACHE_SIZE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSION_CACHE_TIMEOUTS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_SESSIONS_REUSED', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_USED_SESSION_CACHE_ENTRIES', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_VERIFY_DEPTH', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_VERIFY_MODE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('SSL_VERSION', '');
INSERT INTO `SESSION_STATUS` VALUES ('TABLE_LOCKS_IMMEDIATE', '295190');
INSERT INTO `SESSION_STATUS` VALUES ('TABLE_LOCKS_WAITED', '0');
INSERT INTO `SESSION_STATUS` VALUES ('TC_LOG_MAX_PAGES_USED', '0');
INSERT INTO `SESSION_STATUS` VALUES ('TC_LOG_PAGE_SIZE', '0');
INSERT INTO `SESSION_STATUS` VALUES ('TC_LOG_PAGE_WAITS', '0');
INSERT INTO `SESSION_STATUS` VALUES ('THREADS_CACHED', '0');
INSERT INTO `SESSION_STATUS` VALUES ('THREADS_CONNECTED', '1');
INSERT INTO `SESSION_STATUS` VALUES ('THREADS_CREATED', '13052872');
INSERT INTO `SESSION_STATUS` VALUES ('THREADS_RUNNING', '1');
INSERT INTO `SESSION_STATUS` VALUES ('UPTIME', '7162862');
INSERT INTO `SESSION_STATUS` VALUES ('UPTIME_SINCE_FLUSH_STATUS', '7162862');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `SESSION_VARIABLES`
-- 

CREATE TEMPORARY TABLE `SESSION_VARIABLES` (
  `VARIABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `VARIABLE_VALUE` varchar(1024) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `SESSION_VARIABLES`
-- 

INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_PREPARED_STMT_COUNT', '16382');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SETS_DIR', '/usr/share/mysql/charsets/');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_CRYPT', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('CONNECT_TIMEOUT', '10');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_REPAIR_THREADS', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('AUTOMATIC_SP_PRIVILEGES', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_CONNECT_ERRORS', '10');
INSERT INTO `SESSION_VARIABLES` VALUES ('BINLOG_CACHE_SIZE', '32768');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_CONCURRENCY_TICKETS', '500');
INSERT INTO `SESSION_VARIABLES` VALUES ('BACK_LOG', '50');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_CONNECTIONS', '151');
INSERT INTO `SESSION_VARIABLES` VALUES ('STORAGE_ENGINE', 'MyISAM');
INSERT INTO `SESSION_VARIABLES` VALUES ('DELAYED_INSERT_TIMEOUT', '300');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_LENGTH_FOR_SORT_DATA', '1024');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_BIN_TRUST_ROUTINE_CREATORS', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_CONNECTION', 'utf8');
INSERT INTO `SESSION_VARIABLES` VALUES ('SSL_CIPHER', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_RESULTS', 'utf8');
INSERT INTO `SESSION_VARIABLES` VALUES ('BASEDIR', '/usr/');
INSERT INTO `SESSION_VARIABLES` VALUES ('LARGE_PAGES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('UPDATABLE_VIEWS_WITH_LIMIT', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('TABLE_DEFINITION_CACHE', '256');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLOW_LAUNCH_TIME', '2');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_ALLOC_BLOCK_SIZE', '8192');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_LOCK_WAIT_TIMEOUT', '50');
INSERT INTO `SESSION_VARIABLES` VALUES ('COMPLETION_TYPE', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('RELAY_LOG_INDEX', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('COLLATION_SERVER', 'latin1_swedish_ci');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_QUOTE_SHOW_CREATE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('EVENT_SCHEDULER', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_LOG_UPDATE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('COLLATION_DATABASE', 'latin1_swedish_ci');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_USE_LEGACY_CARDINALITY_ALGORITHM', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('FT_MAX_WORD_LEN', '84');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOCKED_IN_MEMORY', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('CONCURRENT_INSERT', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_RECOVER_OPTIONS', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('NET_WRITE_TIMEOUT', '60');
INSERT INTO `SESSION_VARIABLES` VALUES ('REPORT_HOST', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('AUTO_INCREMENT_OFFSET', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('SSL_KEY', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('FLUSH', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_DATABASE', 'latin1');
INSERT INTO `SESSION_VARIABLES` VALUES ('DELAYED_INSERT_LIMIT', '100');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_SKIP_ERRORS', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('FT_QUERY_EXPANSION_LIMIT', '20');
INSERT INTO `SESSION_VARIABLES` VALUES ('INSERT_ID', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_LOW_PRIORITY_UPDATES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('RELAY_LOG_PURGE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('LANGUAGE', '/usr/share/mysql/english/');
INSERT INTO `SESSION_VARIABLES` VALUES ('SKIP_SHOW_DATABASE', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('KEY_CACHE_AGE_THRESHOLD', '300');
INSERT INTO `SESSION_VARIABLES` VALUES ('GROUP_CONCAT_MAX_LEN', '1024');
INSERT INTO `SESSION_VARIABLES` VALUES ('JOIN_BUFFER_SIZE', '131072');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_LOG_BUFFER_SIZE', '1048576');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('VERSION_COMPILE_MACHINE', 'i386');
INSERT INTO `SESSION_VARIABLES` VALUES ('READ_BUFFER_SIZE', '131072');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_MAX_PURGE_LAG', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('DELAYED_QUEUE_SIZE', '1000');
INSERT INTO `SESSION_VARIABLES` VALUES ('TRANSACTION_PREALLOC_SIZE', '4096');
INSERT INTO `SESSION_VARIABLES` VALUES ('INTERACTIVE_TIMEOUT', '28800');
INSERT INTO `SESSION_VARIABLES` VALUES ('VERSION_COMPILE_OS', 'redhat-linux-gnu');
INSERT INTO `SESSION_VARIABLES` VALUES ('WAIT_TIMEOUT', '28800');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_NOTES', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('TABLE_OPEN_CACHE', '64');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOW_PRIORITY_UPDATES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('LARGE_PAGE_SIZE', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('REPORT_PASSWORD', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('INIT_SLAVE', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_INSERT_DELAYED_THREADS', '20');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_BINLOG_SIZE', '1073741824');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_COMPRESS', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_ERROR_COUNT', '64');
INSERT INTO `SESSION_VARIABLES` VALUES ('TRANSACTION_ALLOC_BLOCK_SIZE', '8192');
INSERT INTO `SESSION_VARIABLES` VALUES ('FT_MIN_WORD_LEN', '4');
INSERT INTO `SESSION_VARIABLES` VALUES ('THREAD_CACHE_SIZE', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_WARNINGS', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_DOUBLEWRITE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_LOG_OFF', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('PSEUDO_THREAD_ID', '13052872');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_LONG_DATA_SIZE', '1048576');
INSERT INTO `SESSION_VARIABLES` VALUES ('DEFAULT_WEEK_FORMAT', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_FLUSH_METHOD', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_OUTPUT', 'FILE');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOWER_CASE_TABLE_NAMES', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_RELAY_LOG_SIZE', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_STATS_METHOD', 'nulls_unequal');
INSERT INTO `SESSION_VARIABLES` VALUES ('PROTOCOL_VERSION', '10');
INSERT INTO `SESSION_VARIABLES` VALUES ('NET_RETRY_COUNT', '10');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_SYMLINK', 'DISABLED');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_SORT_LENGTH', '1024');
INSERT INTO `SESSION_VARIABLES` VALUES ('TIME_ZONE', 'SYSTEM');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_HEAP_TABLE_SIZE', '16777216');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_TABLE_LOCKS', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('FT_BOOLEAN_SYNTAX', '+ -><()~*:""&|');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_AUTOEXTEND_INCREMENT', '8');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_QUERIES_NOT_USING_INDEXES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_DATA_POINTER_SIZE', '6');
INSERT INTO `SESSION_VARIABLES` VALUES ('NET_BUFFER_LENGTH', '16384');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_THREAD_SLEEP_DELAY', '10000');
INSERT INTO `SESSION_VARIABLES` VALUES ('FT_STOPWORD_FILE', '(built-in)');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_AUTO_IS_NULL', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('DELAY_KEY_WRITE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_TRANSACTION_RETRIES', '10');
INSERT INTO `SESSION_VARIABLES` VALUES ('MULTI_RANGE_COUNT', '256');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_ERROR', '/var/log/mysqld.log');
INSERT INTO `SESSION_VARIABLES` VALUES ('LONG_QUERY_TIME', '10.000000');
INSERT INTO `SESSION_VARIABLES` VALUES ('GENERAL_LOG', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('BULK_INSERT_BUFFER_SIZE', '8388608');
INSERT INTO `SESSION_VARIABLES` VALUES ('TABLE_TYPE', 'MyISAM');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_MAX_ALLOWED_PACKET', '1073741824');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_CACHE_MIN_RES_UNIT', '4096');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_USE_MMAP', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_STATS_ON_METADATA', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_BINLOG_CACHE_SIZE', '4294963200');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_ROLLBACK_ON_TIMEOUT', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_CACHE_TYPE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('PROFILING_HISTORY_SIZE', '15');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_FILESYSTEM', 'binary');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_ADDITIONAL_MEM_POOL_SIZE', '1048576');
INSERT INTO `SESSION_VARIABLES` VALUES ('TX_ISOLATION', 'REPEATABLE-READ');
INSERT INTO `SESSION_VARIABLES` VALUES ('READ_RND_BUFFER_SIZE', '262144');
INSERT INTO `SESSION_VARIABLES` VALUES ('THREAD_HANDLING', 'one-thread-per-connection');
INSERT INTO `SESSION_VARIABLES` VALUES ('SECURE_AUTH', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('BINLOG_DIRECT_NON_TRANSACTIONAL_UPDATES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('BIG_TABLES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('FLUSH_TIME', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_SELECT_LIMIT', '18446744073709551615');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_INNODB', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('DATE_FORMAT', '%Y-%m-%d');
INSERT INTO `SESSION_VARIABLES` VALUES ('OLD_PASSWORDS', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('READ_ONLY', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_PREALLOC_SIZE', '8192');
INSERT INTO `SESSION_VARIABLES` VALUES ('RAND_SEED1', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLOW_QUERY_LOG_FILE', '/var/run/mysqld/mysqld-slow.log');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_DATA_FILE_PATH', 'ibdata1:10M:autoextend');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_SEEKS_FOR_KEY', '4294967295');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_CACHE_LIMIT', '1048576');
INSERT INTO `SESSION_VARIABLES` VALUES ('SYSTEM_TIME_ZONE', 'CEST');
INSERT INTO `SESSION_VARIABLES` VALUES ('PROFILING', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('SSL_CERT', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_CACHE_SIZE', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_MAX_SORT_FILE_SIZE', '2146435072');
INSERT INTO `SESSION_VARIABLES` VALUES ('KEY_CACHE_DIVISION_LIMIT', '100');
INSERT INTO `SESSION_VARIABLES` VALUES ('THREAD_STACK', '196608');
INSERT INTO `SESSION_VARIABLES` VALUES ('GENERAL_LOG_FILE', '/var/run/mysqld/mysqld.log');
INSERT INTO `SESSION_VARIABLES` VALUES ('OPEN_FILES_LIMIT', '1024');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_SORT_BUFFER_SIZE', '8388608');
INSERT INTO `SESSION_VARIABLES` VALUES ('SKIP_NAME_RESOLVE', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('AUTO_INCREMENT_INCREMENT', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('LAST_INSERT_ID', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('TMPDIR', '/tmp');
INSERT INTO `SESSION_VARIABLES` VALUES ('PID_FILE', '/var/run/mysqld/mysqld.pid');
INSERT INTO `SESSION_VARIABLES` VALUES ('EXPIRE_LOGS_DAYS', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_TMP_TABLES', '32');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_PARTITIONING', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('MIN_EXAMINED_ROW_LIMIT', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('FOREIGN_KEY_CHECKS', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('LARGE_FILES_SUPPORT', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('RELAY_LOG_INFO_FILE', 'relay-log.info');
INSERT INTO `SESSION_VARIABLES` VALUES ('SSL_CAPATH', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_AUTOINC_LOCK_MODE', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('VERSION_COMMENT', 'Source distribution');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_COMMIT_CONCURRENCY', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('OPTIMIZER_SWITCH', 'index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_MIRRORED_LOG_GROUPS', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('OPTIMIZER_PRUNE_LEVEL', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('UNIQUE_CHECKS', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('QUERY_CACHE_WLOCK_INVALIDATE', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('VERSION', '5.1.73');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_WRITE_LOCK_COUNT', '4294967295');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_SUPPORT_XA', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('TIMED_MUTEXES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_SYNC_SPIN_LOOPS', '20');
INSERT INTO `SESSION_VARIABLES` VALUES ('INIT_FILE', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('COLLATION_CONNECTION', 'utf8_general_ci');
INSERT INTO `SESSION_VARIABLES` VALUES ('LC_TIME_NAMES', 'en_US');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_QUERY_CACHE', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('SERVER_ID', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_ADAPTIVE_HASH_INDEX', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('SKIP_NETWORKING', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('RPL_RECOVERY_RANK', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_SYSTEM', 'utf8');
INSERT INTO `SESSION_VARIABLES` VALUES ('DIV_PRECISION_INCREMENT', '4');
INSERT INTO `SESSION_VARIABLES` VALUES ('INIT_CONNECT', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('DATADIR', '/var/lib/mysql/');
INSERT INTO `SESSION_VARIABLES` VALUES ('OPTIMIZER_SEARCH_DEPTH', '62');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_DATA_HOME_DIR', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('AUTOCOMMIT', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('SKIP_EXTERNAL_LOCKING', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('KEY_CACHE_BLOCK_SIZE', '1024');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_SLAVE_SKIP_COUNTER', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_BIG_TABLES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('TIME_FORMAT', '%H:%i:%s');
INSERT INTO `SESSION_VARIABLES` VALUES ('TMP_TABLE_SIZE', '16777216');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_FORCE_RECOVERY', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('TABLE_LOCK_WAIT_TIMEOUT', '50');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_LOG_FILES_IN_GROUP', '2');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_SERVER', 'latin1');
INSERT INTO `SESSION_VARIABLES` VALUES ('ERROR_COUNT', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('HOSTNAME', 'oraclepr.priv.uco.es');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_DYNAMIC_LOADING', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_BUFFER_RESULT', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('SYNC_BINLOG', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_BIN', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('RELAY_LOG_SPACE_LIMIT', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_MODE', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_OPEN_FILES', '300');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_USER_CONNECTIONS', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('OLD', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_EXEC_MODE', 'STRICT');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_MAX_DIRTY_PAGES_PCT', '90');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_MAX_JOIN_SIZE', '18446744073709551615');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_COMMUNITY_FEATURES', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('NEW', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('KEY_BUFFER_SIZE', '8384512');
INSERT INTO `SESSION_VARIABLES` VALUES ('SECURE_FILE_PRIV', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_NDBCLUSTER', 'NO');
INSERT INTO `SESSION_VARIABLES` VALUES ('KEEP_FILES_ON_CREATE', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('REPORT_PORT', '3306');
INSERT INTO `SESSION_VARIABLES` VALUES ('REPORT_USER', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_STATS_METHOD', 'nulls_equal');
INSERT INTO `SESSION_VARIABLES` VALUES ('ENGINE_CONDITION_PUSHDOWN', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_FAST_SHUTDOWN', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_LOG_BIN', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('SSL_CA', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOWER_CASE_FILE_SYSTEM', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_THREAD_CONCURRENCY', '8');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_CSV', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_NET_TIMEOUT', '3600');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_SAFE_UPDATES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_BUFFER_POOL_SIZE', '8388608');
INSERT INTO `SESSION_VARIABLES` VALUES ('PRELOAD_BUFFER_SIZE', '32768');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOCAL_INFILE', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_COMPRESSED_PROTOCOL', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_RTREE_KEYS', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_GEOMETRY', 'YES');
INSERT INTO `SESSION_VARIABLES` VALUES ('BINLOG_FORMAT', 'STATEMENT');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_SP_RECURSION_DEPTH', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('OLD_ALTER_TABLE', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('RELAY_LOG', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('MYISAM_MMAP_SIZE', '4294967295');
INSERT INTO `SESSION_VARIABLES` VALUES ('PLUGIN_DIR', '/usr/lib/mysql/plugin');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_SLOW_QUERIES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('IGNORE_BUILTIN_INNODB', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('TIMESTAMP', '1760263578');
INSERT INTO `SESSION_VARIABLES` VALUES ('NET_READ_TIMEOUT', '30');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_DELAYED_THREADS', '20');
INSERT INTO `SESSION_VARIABLES` VALUES ('SYNC_FRM', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('DATETIME_FORMAT', '%Y-%m-%d %H:%i:%s');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLOW_QUERY_LOG', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_FLUSH_LOG_AT_TRX_COMMIT', '1');
INSERT INTO `SESSION_VARIABLES` VALUES ('WARNING_COUNT', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_FILE_IO_THREADS', '4');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_ALLOWED_PACKET', '1048576');
INSERT INTO `SESSION_VARIABLES` VALUES ('LICENSE', 'GPL');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_BIG_SELECTS', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('RANGE_ALLOC_BLOCK_SIZE', '4096');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_CHECKSUMS', 'ON');
INSERT INTO `SESSION_VARIABLES` VALUES ('PORT', '3306');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_LOCKS_UNSAFE_FOR_BINLOG', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_SSL', 'DISABLED');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_LOG_FILE_SIZE', '5242880');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_BIN_TRUST_FUNCTION_CREATORS', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('LOG_SLAVE_UPDATES', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('MAX_JOIN_SIZE', '18446744073709551615');
INSERT INTO `SESSION_VARIABLES` VALUES ('SORT_BUFFER_SIZE', '2097144');
INSERT INTO `SESSION_VARIABLES` VALUES ('HAVE_OPENSSL', 'DISABLED');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_FILE_PER_TABLE', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('INNODB_LOG_GROUP_HOME_DIR', './');
INSERT INTO `SESSION_VARIABLES` VALUES ('SOCKET', '/var/lib/mysql/mysql.sock');
INSERT INTO `SESSION_VARIABLES` VALUES ('CHARACTER_SET_CLIENT', 'utf8');
INSERT INTO `SESSION_VARIABLES` VALUES ('RAND_SEED2', '');
INSERT INTO `SESSION_VARIABLES` VALUES ('IDENTITY', '0');
INSERT INTO `SESSION_VARIABLES` VALUES ('SQL_WARNINGS', 'OFF');
INSERT INTO `SESSION_VARIABLES` VALUES ('SLAVE_LOAD_TMPDIR', '/tmp');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `STATISTICS`
-- 

CREATE TEMPORARY TABLE `STATISTICS` (
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `NON_UNIQUE` bigint(1) NOT NULL DEFAULT '0',
  `INDEX_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `INDEX_NAME` varchar(64) NOT NULL DEFAULT '',
  `SEQ_IN_INDEX` bigint(2) NOT NULL DEFAULT '0',
  `COLUMN_NAME` varchar(64) NOT NULL DEFAULT '',
  `COLLATION` varchar(1) DEFAULT NULL,
  `CARDINALITY` bigint(21) DEFAULT NULL,
  `SUB_PART` bigint(3) DEFAULT NULL,
  `PACKED` varchar(10) DEFAULT NULL,
  `NULLABLE` varchar(3) NOT NULL DEFAULT '',
  `INDEX_TYPE` varchar(16) NOT NULL DEFAULT '',
  `COMMENT` varchar(16) DEFAULT NULL
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `STATISTICS`
-- 

INSERT INTO `STATISTICS` VALUES (NULL, 'i32esrah', 'socios', 0, 'i32esrah', 'PRIMARY', 1, 'id', 'A', 3, NULL, NULL, '', 'BTREE', '');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `TABLES`
-- 

CREATE TEMPORARY TABLE `TABLES` (
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `TABLE_TYPE` varchar(64) NOT NULL DEFAULT '',
  `ENGINE` varchar(64) DEFAULT NULL,
  `VERSION` bigint(21) unsigned DEFAULT NULL,
  `ROW_FORMAT` varchar(10) DEFAULT NULL,
  `TABLE_ROWS` bigint(21) unsigned DEFAULT NULL,
  `AVG_ROW_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `MAX_DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `INDEX_LENGTH` bigint(21) unsigned DEFAULT NULL,
  `DATA_FREE` bigint(21) unsigned DEFAULT NULL,
  `AUTO_INCREMENT` bigint(21) unsigned DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CHECK_TIME` datetime DEFAULT NULL,
  `TABLE_COLLATION` varchar(32) DEFAULT NULL,
  `CHECKSUM` bigint(21) unsigned DEFAULT NULL,
  `CREATE_OPTIONS` varchar(255) DEFAULT NULL,
  `TABLE_COMMENT` varchar(80) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `TABLES`
-- 

INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'CHARACTER_SETS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 384, 0, 16604160, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=43690', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'COLLATIONS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 231, 0, 16704765, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=72628', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'COLLATION_CHARACTER_SET_APPLICABILITY', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 195, 0, 16691610, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=86037', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'COLUMNS', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=4560', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'COLUMN_PRIVILEGES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 2565, 0, 16757145, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=6540', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'ENGINES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 490, 0, 16709000, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=34239', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'EVENTS', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=618', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'FILES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 2677, 0, 16758020, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=6267', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'GLOBAL_STATUS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 3268, 0, 16755036, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=5133', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'GLOBAL_VARIABLES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 3268, 0, 16755036, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=5133', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'KEY_COLUMN_USAGE', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 4637, 0, 16762755, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=3618', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'PARTITIONS', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=5612', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'PLUGINS', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=13025', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'PROCESSLIST', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=23899', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'PROFILING', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 308, 0, 16562084, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=54471', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'REFERENTIAL_CONSTRAINTS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 4814, 0, 16767162, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=3485', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'ROUTINES', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=588', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'SCHEMATA', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 3464, 0, 16755368, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=4843', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'SCHEMA_PRIVILEGES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 2179, 0, 16767405, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=7699', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'SESSION_STATUS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 3268, 0, 16755036, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=5133', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'SESSION_VARIABLES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 3268, 0, 16755036, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=5133', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'STATISTICS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 2679, 0, 16770540, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=6262', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'TABLES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 3545, 0, 16760760, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=4732', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'TABLE_CONSTRAINTS', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 2504, 0, 16749256, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=6700', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'TABLE_PRIVILEGES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 2372, 0, 16748692, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=7073', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'TRIGGERS', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=569', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'USER_PRIVILEGES', 'SYSTEM VIEW', 'MEMORY', 10, 'Fixed', NULL, 1986, 0, 16759854, 0, 0, NULL, '2025-10-12 12:06:18', NULL, NULL, 'utf8_general_ci', NULL, 'max_rows=8447', '');
INSERT INTO `TABLES` VALUES (NULL, 'information_schema', 'VIEWS', 'SYSTEM VIEW', 'MyISAM', 10, 'Dynamic', NULL, 0, 0, 281474976710655, 1024, 0, NULL, '2025-10-12 12:06:18', '2025-10-12 12:06:18', NULL, 'utf8_general_ci', NULL, 'max_rows=6932', '');
INSERT INTO `TABLES` VALUES (NULL, 'i32esrah', 'socios', 'BASE TABLE', 'InnoDB', 10, 'Compact', 3, 5461, 16384, 0, 0, 1435500544, NULL, '2025-10-11 15:28:45', NULL, NULL, 'utf8_unicode_ci', NULL, '', '');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `TABLE_CONSTRAINTS`
-- 

CREATE TEMPORARY TABLE `TABLE_CONSTRAINTS` (
  `CONSTRAINT_CATALOG` varchar(512) DEFAULT NULL,
  `CONSTRAINT_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `CONSTRAINT_NAME` varchar(64) NOT NULL DEFAULT '',
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `CONSTRAINT_TYPE` varchar(64) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `TABLE_CONSTRAINTS`
-- 

INSERT INTO `TABLE_CONSTRAINTS` VALUES (NULL, 'i32esrah', 'PRIMARY', 'i32esrah', 'socios', 'PRIMARY KEY');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `TABLE_PRIVILEGES`
-- 

CREATE TEMPORARY TABLE `TABLE_PRIVILEGES` (
  `GRANTEE` varchar(81) NOT NULL DEFAULT '',
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `PRIVILEGE_TYPE` varchar(64) NOT NULL DEFAULT '',
  `IS_GRANTABLE` varchar(3) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `TABLE_PRIVILEGES`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `TRIGGERS`
-- 

CREATE TEMPORARY TABLE `TRIGGERS` (
  `TRIGGER_CATALOG` varchar(512) DEFAULT NULL,
  `TRIGGER_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TRIGGER_NAME` varchar(64) NOT NULL DEFAULT '',
  `EVENT_MANIPULATION` varchar(6) NOT NULL DEFAULT '',
  `EVENT_OBJECT_CATALOG` varchar(512) DEFAULT NULL,
  `EVENT_OBJECT_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `EVENT_OBJECT_TABLE` varchar(64) NOT NULL DEFAULT '',
  `ACTION_ORDER` bigint(4) NOT NULL DEFAULT '0',
  `ACTION_CONDITION` longtext,
  `ACTION_STATEMENT` longtext NOT NULL,
  `ACTION_ORIENTATION` varchar(9) NOT NULL DEFAULT '',
  `ACTION_TIMING` varchar(6) NOT NULL DEFAULT '',
  `ACTION_REFERENCE_OLD_TABLE` varchar(64) DEFAULT NULL,
  `ACTION_REFERENCE_NEW_TABLE` varchar(64) DEFAULT NULL,
  `ACTION_REFERENCE_OLD_ROW` varchar(3) NOT NULL DEFAULT '',
  `ACTION_REFERENCE_NEW_ROW` varchar(3) NOT NULL DEFAULT '',
  `CREATED` datetime DEFAULT NULL,
  `SQL_MODE` varchar(8192) NOT NULL DEFAULT '',
  `DEFINER` varchar(77) NOT NULL DEFAULT '',
  `CHARACTER_SET_CLIENT` varchar(32) NOT NULL DEFAULT '',
  `COLLATION_CONNECTION` varchar(32) NOT NULL DEFAULT '',
  `DATABASE_COLLATION` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `TRIGGERS`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `USER_PRIVILEGES`
-- 

CREATE TEMPORARY TABLE `USER_PRIVILEGES` (
  `GRANTEE` varchar(81) NOT NULL DEFAULT '',
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `PRIVILEGE_TYPE` varchar(64) NOT NULL DEFAULT '',
  `IS_GRANTABLE` varchar(3) NOT NULL DEFAULT ''
) ENGINE=MEMORY DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `USER_PRIVILEGES`
-- 

INSERT INTO `USER_PRIVILEGES` VALUES ('''i32esrah''@''%''', NULL, 'USAGE', 'NO');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `VIEWS`
-- 

CREATE TEMPORARY TABLE `VIEWS` (
  `TABLE_CATALOG` varchar(512) DEFAULT NULL,
  `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
  `VIEW_DEFINITION` longtext NOT NULL,
  `CHECK_OPTION` varchar(8) NOT NULL DEFAULT '',
  `IS_UPDATABLE` varchar(3) NOT NULL DEFAULT '',
  `DEFINER` varchar(77) NOT NULL DEFAULT '',
  `SECURITY_TYPE` varchar(7) NOT NULL DEFAULT '',
  `CHARACTER_SET_CLIENT` varchar(32) NOT NULL DEFAULT '',
  `COLLATION_CONNECTION` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 
-- Volcar la base de datos para la tabla `VIEWS`
-- 

