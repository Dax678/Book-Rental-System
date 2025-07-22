-- === USERS ===
INSERT INTO users (full_name, password, email, user_type, created_at, updated_at)
VALUES ('Jan Kowalski', '$2a$10$wjgD5ETqGxkbTNYW3r5Y4umSD9sWG6NWacos50OarutGcsnXzX8z.', 'jan@example.com', 'ADMIN', '2025-01-01', '2025-01-01'),
       ('Anna Nowak', '$2a$10$wjgD5ETqGxkbTNYW3r5Y4umSD9sWG6NWacos50OarutGcsnXzX8z.','anna@example.com', 'LIBRARIAN', '2025-01-01', '2025-01-01'),
       ('Kasia Zielińska', '$2a$10$wjgD5ETqGxkbTNYW3r5Y4umSD9sWG6NWacos50OarutGcsnXzX8z.','kasia@example.com', 'STUDENT', '2025-01-01', '2025-01-01'),
       ('Piotr Wiśniewski', '$2a$10$wjgD5ETqGxkbTNYW3r5Y4umSD9sWG6NWacos50OarutGcsnXzX8z.', 'piotr@example.com', 'CUSTOMER', '2025-01-01', '2025-01-01'),
       ('Zofia Lewandowska', '$2a$10$wjgD5ETqGxkbTNYW3r5Y4umSD9sWG6NWacos50OarutGcsnXzX8z.', 'zofia@example.com', 'CUSTOMER', '2025-01-01', '2025-01-01');

-- === BOOKS ===
INSERT INTO books (title, author, isbn, available)
VALUES ('Clean Code', 'Robert C. Martin', '9780132350884', true),
       ('Effective Java', 'Joshua Bloch', '9780134685991', true),
       ('Spring in Action', 'Craig Walls', '9781617294945', false),
       ('Thinking in Java', 'Bruce Eckel', '9780131872486', true),
       ('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', true),
       ('The Pragmatic Programmer', 'Andy Hunt', '9780201616224', true),
       ('Refactoring', 'Martin Fowler', '9780134757599', true),
       ('Test-Driven Development', 'Kent Beck', '9780321146533', false),
       ('Domain-Driven Design', 'Eric Evans', '9780321125217', true),
       ('Head First Design Patterns', 'Eric Freeman', '9780596007126', true);

-- === RENTALS ===
-- ID użytkowników: 1–5, ID książek: 1–10
INSERT INTO rentals (user_id, book_id, rented_at, due_date, return_date, returned)
VALUES (1, 3, '2025-07-01', '2025-07-10', NULL, false),        -- aktualnie wypożyczona
       (2, 8, '2025-06-20', '2025-06-30', '2025-06-29', true), -- oddana na czas
       (3, 5, '2025-06-15', '2025-06-25', '2025-06-28', true), -- oddana po terminie
       (4, 2, '2025-07-05', '2025-07-15', NULL, false),        -- aktualnie wypożyczona
       (5, 6, '2025-06-01', '2025-06-10', '2025-06-10', true); -- oddana na czas
