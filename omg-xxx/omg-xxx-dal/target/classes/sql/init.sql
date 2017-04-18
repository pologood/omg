CREATE DATABASE `eif_xxx` DEFAULT CHARSET=utf8;

USE `eif_xxx`;

CREATE TABLE `t_xxx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` char(16),
  `name` varchar(20),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;