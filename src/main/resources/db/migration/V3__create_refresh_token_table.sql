CREATE TABLE refresh_token
(
    id UUID PRIMARY KEY,
    token TEXT UNIQUE NOT NULL,
    account_id UUID NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,

    FOREIGN KEY (account_id) REFERENCES account (id)
);