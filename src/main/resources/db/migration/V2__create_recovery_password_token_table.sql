CREATE TABLE recovery_password_token
(
    id UUID NOT NULL PRIMARY KEY,
    account_id UUID NOT NULL,
    token TEXT UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,

    FOREIGN KEY (account_id) REFERENCES account (id)
);