-- liquibase formated sql
-- changeset sgorban:1
     CREATE TABLE notification_task (
         id SERIAL primary key not null ,
         chat_id BIGINT,
         message TEXT,
         date TIMESTAMP
     )
