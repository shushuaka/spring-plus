-- 데이터베이스 생성
CREATE DATABASE springplus;

-- 데이터베이스 사용
USE springplus;

-- User 테이블 생성
CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) UNIQUE    NOT NULL,
    password   VARCHAR(255)           NOT NULL,
    nickname   VARCHAR(255),
    user_role  ENUM ('USER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 샘플 데이터 삽입
INSERT INTO users (email, password, nickname, user_role)
VALUES ('john.doe@example.com', 'password123', 'JohnDoe', 'USER'),
       ('jane.doe@example.com', 'password456', 'JaneDoe', 'ADMIN');
