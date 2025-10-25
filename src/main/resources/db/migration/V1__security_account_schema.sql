-- =========================
-- SECURITY / ACCOUNTS
-- =========================
CREATE TABLE IF NOT EXISTS accounts
(
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(32)  NOT NULL,
    is_active     BOOLEAN      NOT NULL DEFAULT true,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT ck_accounts_role CHECK (role IN ('EMPLOYEE', 'INVESTOR')),
    CONSTRAINT uq_accounts_email UNIQUE (email)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_accounts_email_lower ON accounts (lower(email));

CREATE TABLE IF NOT EXISTS employees
(
    id         UUID PRIMARY KEY,
    account_id UUID UNIQUE  NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL,
    department VARCHAR(100),
    is_admin   BOOLEAN      NOT NULL DEFAULT false,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_employees_account_id ON employees (account_id);

-- Investors (external users bound to accounts)
CREATE TABLE IF NOT EXISTS investors
(
    id              UUID PRIMARY KEY,
    account_id      UUID UNIQUE NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    type            VARCHAR(16) NOT NULL,
    document_number VARCHAR(20) NOT NULL UNIQUE,
    name            VARCHAR(255),
    company_name    VARCHAR(255),
    trade_name      VARCHAR(255),
    phone_number    VARCHAR(20),
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT ck_investors_type CHECK (type IN ('INDIVIDUAL', 'COMPANY'))
);
CREATE INDEX IF NOT EXISTS idx_investors_account_id ON investors (account_id);

-- Authentication tokens (store only HASH of tokens)
CREATE TABLE IF NOT EXISTS reset_password_tokens
(
    id         UUID PRIMARY KEY,
    account_id UUID        NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    token_hash TEXT UNIQUE NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    used_at    TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_reset_password_tokens_account ON reset_password_tokens (account_id);

CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id         UUID PRIMARY KEY,
    account_id UUID        NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    token_hash TEXT UNIQUE NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITHOUT TIME ZONE,
    user_agent TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_account ON refresh_tokens (account_id);