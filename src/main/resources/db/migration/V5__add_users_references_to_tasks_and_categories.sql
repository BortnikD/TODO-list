-- Add userId to tasks
ALTER TABLE tasks
ADD COLUMN user_id INTEGER REFERENCES users(id);

-- Add userId to categories
ALTER TABLE categories
ADD COLUMN user_id INTEGER REFERENCES users(id);
