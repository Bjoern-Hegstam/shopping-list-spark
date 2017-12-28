CREATE TABLE shopping_list_item (
  id               UUID PRIMARY KEY      NOT NULL,
  shopping_list_id UUID REFERENCES shopping_list (id),
  item_type_id     UUID REFERENCES item_type (id),
  quantity         INT                   NOT NULL,
  in_cart          BOOLEAN DEFAULT FALSE NOT NULL
);