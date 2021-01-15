INSERT INTO ROLES(name) VALUES('USER');
INSERT INTO ROLES(name) VALUES('ADMIN');
INSERT INTO ROLES(name) VALUES('STAFF');

INSERT INTO USERS(email,first_name,last_name,password,username)
VALUES('admin@gmail.com','kimly','nhep','$2a$10$gcyjRBT2zsYeYKBbQ2AYpuxyMBWuPHBIkJXzxMUIhsI1Gao0dSCa.','admin');

Select user_id from users where username = 'admin';

select role_id from roles where name = 'ADMIN';

INSERT INTO users_roles(user_id, role_id) values(user_id,role_id);