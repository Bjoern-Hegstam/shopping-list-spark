-- Recreate domain using VARCHAR(36) instead of UUID

-- Rename old tables
ALTER TABLE application_user
  RENAME TO old_application_user;

ALTER TABLE item_type
  RENAME TO old_item_type;

ALTER TABLE shopping_list
  RENAME TO old_shopping_list;

ALTER TABLE shopping_list_item
  RENAME TO old_shopping_list_item;

-- Create new tables
CREATE TABLE application_user (
  id              VARCHAR(36) PRIMARY KEY,
  username        VARCHAR NOT NULL,
  email           VARCHAR NOT NULL,
  hashed_password VARCHAR NOT NULL,
  salt            VARCHAR NOT NULL,
  verified        BOOLEAN NOT NULL,
  role            VARCHAR NOT NULL
);


CREATE TABLE item_type (
  id   VARCHAR(36) PRIMARY KEY  NOT NULL,
  name CHARACTER VARYING        NOT NULL
);

CREATE TABLE shopping_list (
  id   VARCHAR(36) PRIMARY KEY,
  name CHARACTER VARYING NOT NULL
);

CREATE TABLE shopping_list_item (
  id               VARCHAR(36) PRIMARY KEY      NOT NULL,
  shopping_list_id VARCHAR(36) REFERENCES shopping_list (id),
  item_type_id     VARCHAR(36) REFERENCES item_type (id),
  quantity         INT                          NOT NULL,
  in_cart          BOOLEAN DEFAULT FALSE        NOT NULL
);

-- Copy data
INSERT INTO application_user (
  id,
  username,
  email,
  hashed_password,
  salt,
  verified,
  role
) SELECT
    id,
    username,
    email,
    hashed_password,
    salt,
    verified,
    role
  FROM old_application_user;

INSERT INTO item_type (id, name)
  SELECT
    id,
    name
  FROM old_item_type;

INSERT INTO shopping_list (id, name)
  SELECT
    id,
    name
  FROM old_shopping_list;

INSERT INTO shopping_list_item (
  id,
  shopping_list_id,
  item_type_id,
  quantity,
  in_cart
) SELECT
    id,
    shopping_list_id,
    item_type_id,
    quantity,
    in_cart
  FROM old_shopping_list_item;

-- Drop old tables
DROP TABLE old_shopping_list_item;
DROP TABLE old_shopping_list;
DROP TABLE old_item_type;
DROP TABLE old_application_user;
