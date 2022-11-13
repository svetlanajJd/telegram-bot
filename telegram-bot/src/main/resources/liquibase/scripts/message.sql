-- liquibase formated sql
-- changeset sgorban:1
     CREATE TABLE notification_task (
         id INT,
         message TEXT,
         date DATE
     )