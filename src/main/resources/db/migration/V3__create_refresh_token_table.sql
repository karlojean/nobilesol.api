CREATE TABLE refresh_token
(
    id UUID PRIMARY KEY,
    token TEXT UNIQUE NOT NULL,
    user_id UUID NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id)
);