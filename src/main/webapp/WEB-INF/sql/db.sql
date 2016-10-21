INSERT INTO constructors (name) VALUES ('Стригун');
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('805-16', '2016-08-28', null, null, 'Франция', (select constructors.id from constructors where name='Стригун'));
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('779-16', '2016-08-23', null, null, 'РСД-10', (select constructors.id from constructors where name='Стригун'));
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('776-16', '2016-08-18', null, null, 'НП160', (select constructors.id from constructors where name='Стригун'));
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('775-16', '2016-08-16', null, null, 'Агротек', (select constructors.id from constructors where name='Стригун'));

INSERT INTO constructors (name) VALUES ('Загайнова');
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('675-16', '2016-08-15', null, null, 'Агротек', (select constructors.id from constructors where name='Загайнова'));
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('676-16', '2016-08-18', null, null, 'НП160', (select constructors.id from constructors where name='Загайнова'));
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('679-16', '2016-08-23', null, null, 'РСД-10', (select constructors.id from constructors where name='Загайнова'));
INSERT INTO orders (order_number, plan_date, actual_date, cipher, product_name, constructor_id)
VALUES ('605-16', '2016-08-28', null, null, 'Франция', (select constructors.id from constructors where name='Загайнова'));
