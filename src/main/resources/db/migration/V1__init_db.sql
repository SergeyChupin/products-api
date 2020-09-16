CREATE TABLE products (
  id          BIGSERIAL   PRIMARY KEY,
  name        TEXT,
  price       NUMERIC,
  created_at  TIMESTAMP
);