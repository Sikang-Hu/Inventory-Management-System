USE ims;

INSERT INTO item_category
VALUES (1, 'fruit', 'this is a fruit'),
       (2, 'phone', 'this is a phone'),
       (3, 'laptop', 'this is a laptop'),
       (4, 'car', 'this is a car');

INSERT INTO item
VALUES (1, 1, 'apple'),
       (2, 1, 'dragon fruit'),
       (3, 2, 'iphone x'),
       (4, 3, 'xps 15'),
       (5, 3, 'macbook air');

INSERT INTO vendor
VALUES (1, 'Ward, Shields and Oberbrunner', '5894 Hoeger Pines Suite 241', 'AZ', '19556', NULL),
       (2, 'Olson, Mayert and Kessler', '938 Jast Brook Apt. 535', 'IL', '55080',
        'sales phones and laptop'),
       (3, 'Collins-Purdy', '01070 Alaina Key', 'MT', '63790',
        'only sales iphone x');

INSERT INTO vendor_has_item
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (3, 3);

INSERT INTO retail_store
VALUES (1, 'Wanda Street', 'MA', '02555'),
       (2, 'Vision Street', 'MA', '02156'),
       (3, 'Thanos Street', 'MA', '02133');

INSERT INTO supply_order
VALUES (1, 1, 1, '2019-05-26', '2019-06-07'),
       (2, 1, 1, '2019-06-06', '2019-06-10'),
       (3, 2, 2, '2019-06-23', '2019-07-02'),
       (4, 2, 1, '2019-06-26', '2019-07-08'),
       (5, 1, 2, '2019-07-05', '2019-07-29'),
       (6, 2, 2, '2019-07-18', NULL);

INSERT INTO order_has_item
VALUES (1, 1, 50, 0.2),
       (1, 2, 50, 1.1),
       (2, 1, 100, 0.15),
       (2, 2, 70, 1.05),
       (3, 3, 10, 800),
       (4, 5, 5, 1200),
       (5, 1, 200, 0.15),
       (6, 5, 20, 1100);

INSERT INTO customer
VALUES (1, 'Rod Johnson'),
       (2, 'Meta Jenkins'),
       (3, 'Mr. Paris Miller MD'),
       (4, 'Martina Orn'),
       (5, 'Brisa Hane');

INSERT INTO sale
VALUES (1, 1, 1, '2019-06-09 12:23:11'),
       (2, 1, 1, '2019-06-09 12:33:01'),
       (3, 1, 2, '2019-06-09 19:03:39'),
       (4, 1, 4, '2019-07-11 10:38:10');

INSERT INTO sale_has_item
VALUES (1, 1, 20, 0.3),
       (2, 1, 10, 0.3),
       (2, 2, 5, 2.0),
       (3, 2, 10, 1.8),
       (4, 5, 1, 1499);
