CREATE TABLE workspace (
  id VARCHAR(36) PRIMARY KEY,
  name CHARACTER VARYING NOT NULL,
  created_by CHARACTER VARYING NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE user_in_workspace (
  user_id VARCHAR(36) NOT NULL,
  workspace_id VARCHAR(36) NOT NULL,
  is_default_workspace BOOLEAN NOT NULL,
  created_by CHARACTER VARYING NOT NULL,
  created_at TIMESTAMP NOT NULL,

  PRIMARY KEY (user_id, workspace_id),

  CONSTRAINT fk__user_in_workspace__user_id__application_user FOREIGN KEY(user_id) REFERENCES application_user(id),
  CONSTRAINT fk__user_in_workspace__workspace_id__workspace FOREIGN KEY(workspace_id) REFERENCES workspace(id)
);

ALTER TABLE shopping_list
    ADD COLUMN workspace_id VARCHAR(36) NULL;

ALTER TABLE shopping_list
    ADD CONSTRAINT fk__shopping_list__workspace_id__workspace FOREIGN KEY(workspace_id) REFERENCES workspace(id);
