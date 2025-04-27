CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    categoryId INT REFERENCES categories(id) ON DELETE CASCADE,
    priority INT,
    text VARCHAR(256),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    is_completed BOOL DEFAULT false,
    completed_at TIMESTAMPTZ  DEFAULT null
);