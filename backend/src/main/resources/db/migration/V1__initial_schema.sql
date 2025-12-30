-- V1__initial_schema.sql

-- Tabela de Usuários (User) - Autenticação simplificada com Role como String
CREATE TABLE "user" (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL -- Agora bate com o @Enumerated do Java
);

-- Tabela de Candidatos
CREATE TABLE candidate (
    id UUID PRIMARY KEY REFERENCES "user"(id),
    name VARCHAR(255),
    cv_url VARCHAR(512),
    current_title VARCHAR(255),
    seniority VARCHAR(50),
    embedding_vector TEXT 
);

-- Tabela Auxiliar para Skills
CREATE TABLE candidate_skills (
    candidate_id UUID REFERENCES candidate(id) ON DELETE CASCADE,
    skill VARCHAR(100),
    PRIMARY KEY (candidate_id, skill)
);

-- Tabela de Vagas
CREATE TABLE job (
    id UUID PRIMARY KEY,
    recruiter_id UUID REFERENCES "user"(id),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    required_skills TEXT,
    job_embedding_vector TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);