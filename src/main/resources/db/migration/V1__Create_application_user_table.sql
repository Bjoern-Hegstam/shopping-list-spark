
CREATE TABLE application_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  hashed_password VARCHAR NOT NULL,
  salt VARCHAR NOT NULL,
  verified BOOLEAN NOT NULL,
  role VARCHAR NOT NULL
)