CREATE TABLE IF NOT EXISTS member (
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    university VARCHAR(100) NOT NULL,
    student_id BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    PRIMARY KEY (member_id)
);


