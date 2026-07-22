CREATE TABLE users (
   id UUID PRIMARY KEY,
   email VARCHAR(255) NOT NULL UNIQUE,
   password_hash VARCHAR(255) NOT NULL,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE NOT NULL,
   updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE user_roles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT uk_user_role
        UNIQUE (user_id, role)
);