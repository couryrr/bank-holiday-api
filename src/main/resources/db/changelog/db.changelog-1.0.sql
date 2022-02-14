--liquibase formatted sql

--changeset couryrr:00001
CREATE TABLE IF NOT EXISTS united_states(
   id INT NOT NULL AUTO_INCREMENT,
   holiday_name VARCHAR(255) NOT NULL,
   holiday_date DATE NOT NULL,
   holiday_year INT NOT NULL,
   PRIMARY KEY ( id )
);

--changeset couryrr:00002
ALTER TABLE united_states
ADD COLUMN hash VARCHAR(32);