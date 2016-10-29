CREATE TABLE constructors
(
   id bigserial PRIMARY KEY,
   name text NOT NULL UNIQUE
);

CREATE TABLE orders
(
   id bigserial PRIMARY KEY,
   order_number text NOT NULL,
   plan_date date  NOT NULL,
   actual_date date,
   cipher text,
   product_name text NOT NULL,
   is_archive boolean DEFAULT FALSE,
   constructor_id bigint NOT NULL,
   FOREIGN KEY (constructor_id) REFERENCES constructors(id)
);

CREATE  TABLE users (
   id bigserial PRIMARY KEY,
   name text NOT NULL,
   password text NOT NULL
);

CREATE  TABLE roles (
  id bigserial PRIMARY KEY,
  name text NOT NULL
);

CREATE TABLE user_role (
   user_id bigint REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
   role_id bigint REFERENCES roles (id) ON UPDATE CASCADE,
   CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id)
);

INSERT INTO users(name, password) VALUES ('admin','$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.');
INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
INSERT INTO roles(name) VALUES ('ROLE_SUPERADMIN');
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO user_role (user_id, role_id) VALUES (1, 3);

CREATE TABLE tasks (
   id bigserial PRIMARY KEY,
   last_executed date  NOT NULL,
   next_execution date  NOT NULL
);

INSERT INTO tasks(last_executed, next_execution) VALUES (to_date(concat('05',' ', extract(month FROM CURRENT_DATE),' ', extract(year FROM CURRENT_DATE)), 'DD MM YYYY'), to_date(concat('05',' ', extract(month FROM CURRENT_DATE + '1 month'::interval),' ', extract(year FROM CURRENT_DATE)), 'DD MM YYYY'));