-- USERS
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    full_name  VARCHAR(100)        NOT NULL,
    password   VARCHAR(100)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    user_type  VARCHAR(10)         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP           NOT NULL
);

-- BOOKS
CREATE TABLE books
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(200)       NOT NULL,
    author    VARCHAR(150)       NOT NULL,
    isbn      VARCHAR(20) UNIQUE NOT NULL,
    available BOOLEAN            NOT NULL DEFAULT true
);

-- RENTALS
CREATE TABLE rentals
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    book_id     BIGINT NOT NULL,
    rented_at   DATE   NOT NULL,
    due_date    DATE   NOT NULL,
    return_date DATE,
    returned    BOOLEAN DEFAULT false,

    CONSTRAINT fk_rental_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_rental_book FOREIGN KEY (book_id) REFERENCES books (id)
);
