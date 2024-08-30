CREATE TABLE CATEGORY (
              id BIGINT AUTO_INCREMENT PRIMARY KEY,
              name VARCHAR(255) NOT NULL,
              queueName VARCHAR(255) NOT NULL
);

CREATE TABLE TICKET
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    createdAt              DATE         NOT NULL,
    issueDescription       VARCHAR(255) NOT NULL,
    status                 VARCHAR(50) NOT NULL
);