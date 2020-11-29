DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
	role_id int GENERATED ALWAYS AS IDENTITY,
	role_name varchar(25),
	PRIMARY KEY(role_id)
);

CREATE TABLE users (
	user_id int GENERATED ALWAYS AS IDENTITY,
	role_id integer,
	first_name varchar(50),
	last_name varchar(50),
	username varchar(25) UNIQUE NOT NULL,
	salt varchar(255),
	password_hash varchar(255),
	PRIMARY KEY(user_id),
	FOREIGN KEY(role_id)
		REFERENCES roles(role_id)
);

INSERT INTO roles (role_name)
VALUES ('User'), ('Admin');

INSERT INTO users (first_name, last_name, username, password_hash, role_id)
VALUES ('Andrew', 'Capp', 'acapp', '1111', 2), 
('Jessica', 'Zhang', 'jessica', '2222', 1);

SELECT * FROM users;

SELECT * FROM roles;

SELECT * FROM users u
INNER JOIN roles r ON
u.role_id = r.role_id;

SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE user_id = 2 LIMIT 1;

SELECT * FROM users WHERE username = 'acapp' AND password_hash = '1111' LIMIT 1;