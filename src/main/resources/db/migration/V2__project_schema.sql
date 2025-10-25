CREATE TABLE IF NOT EXISTS projects (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    rated_power_w BIGINT NOT NULL,
    peak_power_wp BIGINT NOT NULL,
    business_model VARCHAR(20) NOT NULL CHECK (business_model IN ('traditional', 'fractional')),
    energy_distribution_rule VARCHAR(20) CHECK (energy_distribution_rule IN ('mutualist', 'individual')),
    utility_company VARCHAR(255),
    project_status VARCHAR(20) NOT NULL DEFAULT 'planning' CHECK (project_status IN ('planning', 'under_construction', 'operational', 'suspended', 'decommissioned')),
    construction_start_date DATE,
    commercial_operation_date DATE,
    expected_annual_generation_wh BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT ck_lat_range CHECK (latitude BETWEEN -90  AND 90),
    CONSTRAINT ck_lon_range CHECK (longitude BETWEEN -180 AND 180)
);

CREATE TABLE IF NOT EXISTS plants (
    id UUID PRIMARY KEY,
    id_project UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('unitary', 'solar_box')),
    rated_power_w BIGINT NOT NULL,
    peak_power_wp BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'available' CHECK (status IN ('available', 'reserved', 'sold', 'operational')),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (id_project) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS plant_investor (
    id UUID PRIMARY KEY,
    id_plant UUID NOT NULL,
    id_investor UUID NOT NULL,
    linked_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (id_plant) REFERENCES plants(id) ON DELETE CASCADE,
    FOREIGN KEY (id_investor) REFERENCES investors(id) ON DELETE CASCADE,
    UNIQUE (id_plant, id_investor)
);

CREATE INDEX IF NOT EXISTS idx_projects_status ON projects(project_status);
CREATE INDEX IF NOT EXISTS idx_projects_business_model ON projects(business_model);
CREATE INDEX IF NOT EXISTS idx_plants_project ON plants(id_project);
CREATE INDEX IF NOT EXISTS idx_plants_status ON plants(status);
CREATE INDEX IF NOT EXISTS idx_plant_investor_plant ON plant_investor(id_plant);
CREATE INDEX IF NOT EXISTS idx_plant_investor_investor ON plant_investor(id_investor);