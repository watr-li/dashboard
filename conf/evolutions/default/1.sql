# --- !Ups

CREATE TABLE `files` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `path` text NOT NULL,
  `name` text NOT NULL,
  `content_type` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

CREATE TABLE `nodes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `hardware_identifier` varchar(100) NOT NULL DEFAULT '',
  `address` text NOT NULL,
  `port` mediumint(11) unsigned NOT NULL,
  `plant_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hardware_identifier` (`hardware_identifier`),
  KEY `plant_id` (`plant_id`),
  CONSTRAINT `nodes_ibfk_1` FOREIGN KEY (`plant_id`) REFERENCES `plants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

CREATE TABLE `plant_humidities` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `plant_id` int(10) unsigned NOT NULL,
  `humidity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plant_id` (`plant_id`),
  CONSTRAINT `plant_humidities_ibfk_1` FOREIGN KEY (`plant_id`) REFERENCES `plants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `plants` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  `current_state` varchar(25) DEFAULT 'null',
  `image_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `image_id` (`image_id`),
  CONSTRAINT `plants_ibfk_1` FOREIGN KEY (`image_id`) REFERENCES `files` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# --- !Downs

DROP TABLE plant_humidities;
DROP TABLE nodes;
DROP TABLE plants;
DROP TABLE files;
