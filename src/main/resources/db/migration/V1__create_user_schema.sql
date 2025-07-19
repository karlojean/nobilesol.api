CREATE TABLE account
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) UNIQUE      NOT NULL,
    name          VARCHAR(30)              NOT NULL,
    password_hash VARCHAR(255)             NOT NULL,
    role          VARCHAR(50)              NOT NULL,
    is_active     BOOLEAN                  NOT NULL DEFAULT true,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE investors
(
    id              UUID PRIMARY KEY,
    account_id      UUID UNIQUE  NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    full_name       VARCHAR(255) NOT NULL,
    document_number VARCHAR(14)  NOT NULL,
    phone_number    VARCHAR(11)
);

CREATE TABLE employees
(
    id         UUID PRIMARY KEY,
    account_id UUID UNIQUE NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    department VARCHAR(100),
    is_admin   boolean     NOT NULL DEFAULT false
);