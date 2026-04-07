-- Sample data for demo

INSERT INTO books (title, author, isbn, published_date, created_at, available)
VALUES ('Clean Code', 'Robert C. Martin', '9780132350884', '2008-08-01', CURRENT_TIMESTAMP(), true);

INSERT INTO books (title, author, isbn, published_date, created_at, available)
VALUES ('Effective Java', 'Joshua Bloch', '9780134685991', '2018-01-06', CURRENT_TIMESTAMP(), true);

INSERT INTO books (title, author, isbn, published_date, created_at, available)
VALUES ('Design Patterns', 'Gang of Four', '9780201633610', '1994-10-31', CURRENT_TIMESTAMP(), true);

INSERT INTO books (title, author, isbn, published_date, created_at, available)
VALUES ('Spring in Action', 'Craig Walls', '9781617294945', '2018-10-01', CURRENT_TIMESTAMP(), true);

INSERT INTO books (title, author, isbn, published_date, created_at, available)
VALUES ('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', '2006-05-19', CURRENT_TIMESTAMP(), true);

INSERT INTO members (name, email, registered_at, active)
VALUES ('Kovacs Janos', 'kovacs.janos@example.com', CURRENT_TIMESTAMP(), true);

INSERT INTO members (name, email, registered_at, active)
VALUES ('Nagy Anna', 'nagy.anna@example.com', CURRENT_TIMESTAMP(), true);

INSERT INTO members (name, email, registered_at, active)
VALUES ('Szabo Peter', 'szabo.peter@example.com', CURRENT_TIMESTAMP(), true);
