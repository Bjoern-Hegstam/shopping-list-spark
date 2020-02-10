ALTER TABLE item_type
    ADD COLUMN shopping_list_id VARCHAR(36) REFERENCES shopping_list (id);
