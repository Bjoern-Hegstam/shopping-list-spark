-- Add nullable columns
ALTER TABLE shopping_list
  ADD created_at TIMESTAMP;

ALTER TABLE shopping_list
  ADD updated_at TIMESTAMP;

ALTER TABLE shopping_list_item
  ADD created_at TIMESTAMP;

ALTER TABLE shopping_list_item
  ADD updated_at TIMESTAMP;

ALTER TABLE item_type
  ADD created_at TIMESTAMP;

ALTER TABLE item_type
  ADD updated_at TIMESTAMP;

-- Migrate
UPDATE shopping_list
  SET
    created_at = current_timestamp,
    updated_at = current_timestamp;

UPDATE shopping_list_item
SET
  created_at = current_timestamp,
  updated_at = current_timestamp;

UPDATE item_type
SET
  created_at = current_timestamp,
  updated_at = current_timestamp;

-- Make non-nullable
ALTER TABLE shopping_list
  ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE shopping_list
  ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE shopping_list_item
  ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE shopping_list_item
  ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE item_type
  ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE item_type
  ALTER COLUMN updated_at SET NOT NULL;