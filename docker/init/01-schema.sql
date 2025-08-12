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

CREATE TABLE books
(
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(200)       NOT NULL,
    author         VARCHAR(150)       NOT NULL,
    isbn           VARCHAR(20) UNIQUE NOT NULL,
    available      BOOLEAN            NOT NULL DEFAULT true,
    genre          VARCHAR(100)       NOT NULL DEFAULT 'indefinite',
    tags           TEXT[]             NOT NULL DEFAULT ARRAY []::TEXT[],
    rental_price   NUMERIC(10, 2)     NOT NULL,
    purchase_price NUMERIC(10, 2)     NOT NULL
);

CREATE TABLE book_inventory
(
    book_id              BIGINT  NOT NULL
        REFERENCES books (id)
            ON DELETE CASCADE,
    purchase_stock_count INTEGER NOT NULL DEFAULT 0,
    rental_stock_count   INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (book_id)
);

CREATE TABLE book_price_history
(
    price_id   BIGSERIAL PRIMARY KEY,
    book_id    BIGINT         NOT NULL
        REFERENCES books (id)
            ON DELETE CASCADE,
    price      NUMERIC(10, 2) NOT NULL,
    valid_from TIMESTAMP      NOT NULL DEFAULT NOW(),
    valid_to   TIMESTAMP      NULL
);

CREATE TABLE book_promotions
(
    promo_id             BIGSERIAL PRIMARY KEY,
    book_id              BIGINT         NOT NULL
        REFERENCES books (id)
            ON DELETE CASCADE,
    rental_promo_price   NUMERIC(10, 2) NOT NULL,
    purchase_promo_price NUMERIC(10, 2) NOT NULL,
    start_date           TIMESTAMP      NOT NULL,
    end_date             TIMESTAMP      NOT NULL,
    CHECK (end_date >= start_date)
);

CREATE TABLE transactions
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT      NOT NULL
        REFERENCES users (id)
            ON DELETE CASCADE,
    book_id          BIGINT      NOT NULL
        REFERENCES books (id)
            ON DELETE CASCADE,
    transaction_type VARCHAR(10) NOT NULL,
    transaction_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operation_price  NUMERIC(10, 2),
    start_date       TIMESTAMP,
    due_date         TIMESTAMP,
    return_date      TIMESTAMP,
    returned         BOOLEAN              DEFAULT false
);

