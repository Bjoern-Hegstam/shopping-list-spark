CREATE TABLE workspace (
  id UUID PRIMARY KEY,
  name CHARACTER VARYING NOT NULL,
  created_by CHARACTER VARYING NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE user_in_workspace (
  user_id UUID NOT NULL,
  workspace_id UUID NOT NULL,
  is_default_workspace BIT NOT NULL,

  CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES application_user(id),
  CONSTRAINT fk_workspace FOREIGN KEY(workspace_id) REFERENCES workspace(id),
  UNIQUE(user_id, is_default_workspace)
);

ALTER TABLE shopping_list
    ADD COLUMN ;
