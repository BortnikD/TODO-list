-- Add userId to tasks
ALTER TABLE tasks
ADD COLUMN userId INTEGER REFERENCES users(id);

UPDATE tasks
SET userId = 1
where userId IS NULL;

ALTER TABLE tasks ALTER COLUMN userId SET NOT NULL;

-- Add userId to categories
ALTER TABLE categories
    ADD COLUMN userId INTEGER REFERENCES users(id);

UPDATE categories
SET userId = 1
where userId IS NULL;

ALTER TABLE categories ALTER COLUMN userId SET NOT NULL;