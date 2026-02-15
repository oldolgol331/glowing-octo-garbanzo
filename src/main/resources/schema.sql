SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `concerts`;
DROP TABLE IF EXISTS `tickets`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `concerts`
(
    `concert_id` BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL UNIQUE,
    `date`       DATETIME     NOT NULL,
    `max_seats`  INT          NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (`max_seats` > 0)
);

CREATE TABLE `tickets`
(
    `ticket_id`  BIGINT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `concert_id` BIGINT     NOT NULL,
    `user_id`    BINARY(16) NOT NULL,
    `issued_at`  DATETIME   NOT NULL,
    `created_at` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE `uq_tickets_concert_id_user_id` (`concert_id`, `user_id`),
    FOREIGN KEY (`concert_id`) REFERENCES `concerts` (`concert_id`)
);