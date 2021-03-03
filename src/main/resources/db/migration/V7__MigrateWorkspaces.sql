-- Create a default workspace for each user
INSERT INTO workspace (id, name, created_by, created_at)
SELECT id, 'Default workspace', 'MIGRATION', NOW()
FROM application_user;

INSERT INTO user_in_workspace (
  user_id
  ,workspace_id
  ,is_default_workspace
  ,created_by
  ,created_at
)
SELECT id, id, true, 'MIGRATION', NOW()
FROM application_user;

-- Create a migration workspace for all existing shopping lists and give all existing users access to it
INSERT INTO workspace (id, name, created_by, created_at)
values (
  '2a07710d-9047-4653-b348-2776dd42e02f'
  ,'Migration workspace'
  ,'MIGRATION'
  ,NOW()
);

INSERT INTO user_in_workspace (
  user_id
  ,workspace_id
  ,is_default_workspace
  ,created_by
  ,created_at
)
SELECT
   id
   ,'2a07710d-9047-4653-b348-2776dd42e02f'
   ,false
   ,'MIGRATION'
   ,NOW()
FROM application_user;

UPDATE shopping_list SET workspace_id = '2a07710d-9047-4653-b348-2776dd42e02f';

-- Require workspace for new shopping lists
ALTER TABLE shopping_list
    ALTER COLUMN workspace_id SET NOT NULL;
