-- Create allergies catalog table
CREATE TABLE allergies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create dietary restrictions catalog table
CREATE TABLE dietary_restrictions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create client profiles table
CREATE TABLE client_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create client allergies junction table
CREATE TABLE client_allergies (
    id BIGSERIAL PRIMARY KEY,
    client_profile_id BIGINT NOT NULL,
    allergy_id BIGINT NOT NULL,
    severity VARCHAR(20) NOT NULL CHECK (severity IN ('MILD', 'MODERATE', 'SEVERE')),
    notes VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_allergy_profile FOREIGN KEY (client_profile_id) REFERENCES client_profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_allergy_allergy FOREIGN KEY (allergy_id) REFERENCES allergies(id) ON DELETE CASCADE,
    CONSTRAINT uk_client_allergy UNIQUE (client_profile_id, allergy_id)
);

-- Create client restrictions junction table
CREATE TABLE client_restrictions (
    id BIGSERIAL PRIMARY KEY,
    client_profile_id BIGINT NOT NULL,
    restriction_id BIGINT NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_restriction_profile FOREIGN KEY (client_profile_id) REFERENCES client_profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_restriction_restriction FOREIGN KEY (restriction_id) REFERENCES dietary_restrictions(id) ON DELETE CASCADE,
    CONSTRAINT uk_client_restriction UNIQUE (client_profile_id, restriction_id)
);

-- Create client preferences table
CREATE TABLE client_preferences (
    id BIGSERIAL PRIMARY KEY,
    client_profile_id BIGINT NOT NULL UNIQUE,
    meal_plan_type VARCHAR(20) NOT NULL CHECK (meal_plan_type IN ('STANDARD', 'CUSTOM')),
    meals_per_day INTEGER NOT NULL CHECK (meals_per_day BETWEEN 1 AND 3),
    include_breakfast BOOLEAN NOT NULL DEFAULT false,
    include_lunch BOOLEAN NOT NULL DEFAULT false,
    include_dinner BOOLEAN NOT NULL DEFAULT false,
    target_calories INTEGER,
    target_protein INTEGER,
    target_carbs INTEGER,
    target_fats INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_preferences_profile FOREIGN KEY (client_profile_id) REFERENCES client_profiles(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_client_profiles_user_id ON client_profiles(user_id);
CREATE INDEX idx_client_allergies_profile_id ON client_allergies(client_profile_id);
CREATE INDEX idx_client_allergies_allergy_id ON client_allergies(allergy_id);
CREATE INDEX idx_client_restrictions_profile_id ON client_restrictions(client_profile_id);
CREATE INDEX idx_client_restrictions_restriction_id ON client_restrictions(restriction_id);
CREATE INDEX idx_client_preferences_profile_id ON client_preferences(client_profile_id);

-- Insert common allergies
INSERT INTO allergies (name, description) VALUES
('Dairy', 'Lactose intolerance or milk protein allergy'),
('Nuts', 'Tree nuts (almonds, walnuts, cashews, etc.)'),
('Peanuts', 'Peanut allergy'),
('Shellfish', 'Shrimp, crab, lobster, etc.'),
('Fish', 'All types of fish'),
('Eggs', 'Egg allergy'),
('Soy', 'Soy products'),
('Gluten', 'Wheat, barley, rye'),
('Sesame', 'Sesame seeds and oil');

-- Insert common dietary restrictions
INSERT INTO dietary_restrictions (name, description) VALUES
('Vegetarian', 'No meat or fish'),
('Vegan', 'No animal products'),
('Keto', 'Low carb, high fat'),
('Paleo', 'No grains, legumes, or dairy'),
('Low Carb', 'Reduced carbohydrate intake'),
('High Protein', 'Increased protein intake'),
('Halal', 'Islamic dietary laws'),
('Kosher', 'Jewish dietary laws');
