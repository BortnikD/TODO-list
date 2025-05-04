-- Add userId to tasks
ALTER TABLE tasks
ADD COLUMN userId INTEGER REFERENCES users(id);

-- Add userId to categories
ALTER TABLE categories
ADD COLUMN userId INTEGER REFERENCES users(id);
