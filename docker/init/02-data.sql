BEGIN;

INSERT INTO users (id, full_name, password, email, user_type, created_at, updated_at)
VALUES (1, 'Jan Kowalski', '$2a$10$mem6eiFyFuO7ZgmZ/sE2UOR/daZKNn/MFGbv6xPIgP7u6Sd4VuVgC', 'jan.kowalski@example.com', 'CUSTOMER', '2025-01-10 09:15:00',
        '2025-01-10 09:15:00'),
       (2, 'Anna Nowak', '$2a$10$mem6eiFyFuO7ZgmZ/sE2UOR/daZKNn/MFGbv6xPIgP7u6Sd4VuVgC', 'anna.nowak@example.com', 'CUSTOMER', '2025-02-05 14:30:00',
        '2025-02-05 14:30:00'),
       (3, 'Piotr Zieliński', '$2a$10$mem6eiFyFuO7ZgmZ/sE2UOR/daZKNn/MFGbv6xPIgP7u6Sd4VuVgC', 'piotr.zielinski@example.com', 'CUSTOMER', '2025-03-12 11:00:00',
        '2025-03-12 11:00:00'),
       (4, 'Maria Wiśniewska', '$2a$10$mem6eiFyFuO7ZgmZ/sE2UOR/daZKNn/MFGbv6xPIgP7u6Sd4VuVgC', 'admin@example.com', 'ADMIN', '2025-01-20 08:45:00',
        '2025-01-20 08:45:00'),
       (5, 'Katarzyna Lewandowska', '$2a$10$mem6eiFyFuO7ZgmZ/sE2UOR/daZKNn/MFGbv6xPIgP7u6Sd4VuVgC', 'kasia.lewandowska@example.com', 'STUDENT', '2025-04-01 16:20:00',
        '2025-04-01 16:20:00');

INSERT INTO books (id, title, author, isbn, available, genre, tags, rental_price, purchase_price)
VALUES (1, 'W pustyni i w puszczy', 'Henryk Sienkiewicz', '978-83-000-0001-1', true, 'Adventure',
        ARRAY ['classic','bestseller-2025'], 14.90, 59.90),
       (2, 'Pan Tadeusz', 'Adam Mickiewicz', '978-83-000-0002-2', true, 'Epic', ARRAY ['classic'], 9.90, 39.90),
       (3, 'Cyberpunk 2077', 'Various', '978-83-000-0003-3', true, 'Science Fiction',
        ARRAY ['bestseller-2025','top-10'], 19.99, 99.99),
       (4, 'Thriller Nights', 'John Doe', '978-83-000-0004-4', true, 'Thriller', ARRAY ['new-release'], 9.99, 29.90),
       (5, 'Java od podstaw', 'Jan Code', '978-83-000-0005-5', true, 'Educational', ARRAY []::TEXT[], 7.90, 89.00);
INSERT INTO book_inventory (book_id, purchase_stock_count, rental_stock_count)
VALUES (1, 3, 1),
       (2, 0, 2),
       (3, 5, 2),
       (4, 2, 0),
       (5, 10, 0);

INSERT INTO book_price_history (book_id, price, valid_from, valid_to)
VALUES
    -- Book 1: zmiana ceny od początku 2025
    (1, 49.90, '2024-01-01 00:00:00', '2025-01-01 00:00:00'),
    (1, 59.90, '2025-01-01 00:00:00', NULL),
    -- Book 2: jedna cena od lutego 2025
    (2, 39.90, '2025-02-01 00:00:00', NULL),
    -- Book 3: cena od połowy 2024
    (3, 99.99, '2024-06-15 00:00:00', NULL),
    -- Book 4: cena od lipca 2025
    (4, 29.90, '2025-07-01 00:00:00', NULL),
    -- Book 5: promocja półroczna, potem podwyżka
    (5, 79.00, '2025-01-20 00:00:00', '2025-06-01 00:00:00'),
    (5, 89.00, '2025-06-01 00:00:00', NULL);

INSERT INTO book_promotions (book_id, rental_promo_price, purchase_promo_price, start_date, end_date)
VALUES (1, 9.90, 20.90, '2025-01-15', '2025-01-31'),
       (3, 9.99, 25.99,'2025-07-01', '2025-07-15');

INSERT INTO transactions (id, user_id, book_id, transaction_type, transaction_date, operation_price, start_date,
                          due_date, return_date, returned)
VALUES
    -- rent
    (1, 1, 1, 'RENT', '2025-06-01 09:00:00', 14.90, '2025-06-01', '2025-06-15', '2025-06-10', TRUE),
    (2, 3, 3, 'RENT', '2025-07-20 10:30:00', 19.99, '2025-07-20', '2025-08-03', NULL, FALSE),
    (3, 5, 4, 'RENT', '2025-07-25 14:00:00', 9.99, '2025-07-25', '2025-08-08', NULL, FALSE),
    (4, 3, 5, 'RENT', '2025-05-10 11:15:00', 7.90, '2025-05-10', '2025-05-24', '2025-05-22', TRUE),
    (5, 5, 2, 'RENT', '2025-06-10 12:00:00', 9.90, '2025-06-10', '2025-06-24', '2025-06-20', TRUE),
    -- purchase
    (6, 2, 2, 'PURCHASE', '2025-03-01 10:00:00', 39.90, NULL, NULL, NULL, NULL),
    (7, 4, 5, 'PURCHASE', '2025-04-15 16:45:00', 89.00, NULL, NULL, NULL, NULL);

COMMIT;