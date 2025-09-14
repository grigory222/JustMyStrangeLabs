-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS s408402;

-- Create users table
CREATE TABLE s408402.users (
                               id BIGSERIAL PRIMARY KEY,
                               username VARCHAR(255) NOT NULL,
                               password VARCHAR(255) NOT NULL
);

-- Create results_table
CREATE TABLE s408402.results_table (
                                       id BIGSERIAL PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       x INTEGER NOT NULL,
                                       y DOUBLE PRECISION NOT NULL,
                                       r INTEGER NOT NULL,
                                       result BOOLEAN NOT NULL,
                                       FOREIGN KEY (user_id) REFERENCES s408402.users(id) ON DELETE CASCADE
);

-- Create sequences (though they're automatically created with BIGSERIAL)
CREATE SEQUENCE IF NOT EXISTS s408402.users_id_seq;
CREATE SEQUENCE IF NOT EXISTS s408402.results_table_id_seq;

-- Optional: Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_results_user_id ON s408402.results_table(user_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON s408402.users(username);