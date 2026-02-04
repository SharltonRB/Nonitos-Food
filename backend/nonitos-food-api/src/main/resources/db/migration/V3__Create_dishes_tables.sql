-- Create dishes table
CREATE TABLE dishes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    category VARCHAR(20) NOT NULL CHECK (category IN ('BREAKFAST', 'LUNCH', 'DINNER')),
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    calories INTEGER NOT NULL CHECK (calories >= 0),
    protein INTEGER NOT NULL CHECK (protein >= 0),
    carbs INTEGER NOT NULL CHECK (carbs >= 0),
    fats INTEGER NOT NULL CHECK (fats >= 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create dish images table
CREATE TABLE dish_images (
    id BIGSERIAL PRIMARY KEY,
    dish_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT false,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dish_image_dish FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE
);

-- Create dish tags catalog table
CREATE TABLE dish_tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create dish tag assignments junction table
CREATE TABLE dish_tag_assignments (
    id BIGSERIAL PRIMARY KEY,
    dish_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dish_tag_assignment_dish FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
    CONSTRAINT fk_dish_tag_assignment_tag FOREIGN KEY (tag_id) REFERENCES dish_tags(id) ON DELETE CASCADE,
    CONSTRAINT uk_dish_tag UNIQUE (dish_id, tag_id)
);

-- Create dish allergens junction table
CREATE TABLE dish_allergens (
    id BIGSERIAL PRIMARY KEY,
    dish_id BIGINT NOT NULL,
    allergy_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dish_allergen_dish FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
    CONSTRAINT fk_dish_allergen_allergy FOREIGN KEY (allergy_id) REFERENCES allergies(id) ON DELETE CASCADE,
    CONSTRAINT uk_dish_allergen UNIQUE (dish_id, allergy_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_dishes_category ON dishes(category);
CREATE INDEX idx_dishes_is_active ON dishes(is_active);
CREATE INDEX idx_dishes_price ON dishes(price);
CREATE INDEX idx_dish_images_dish_id ON dish_images(dish_id);
CREATE INDEX idx_dish_images_is_primary ON dish_images(is_primary);
CREATE INDEX idx_dish_tag_assignments_dish_id ON dish_tag_assignments(dish_id);
CREATE INDEX idx_dish_tag_assignments_tag_id ON dish_tag_assignments(tag_id);
CREATE INDEX idx_dish_allergens_dish_id ON dish_allergens(dish_id);
CREATE INDEX idx_dish_allergens_allergy_id ON dish_allergens(allergy_id);

-- Insert common dish tags
INSERT INTO dish_tags (name, description) VALUES
('High Protein', 'Dishes with high protein content'),
('Low Carb', 'Dishes with reduced carbohydrates'),
('Spicy', 'Dishes with spicy ingredients'),
('Vegetarian', 'Suitable for vegetarians'),
('Vegan', 'Suitable for vegans'),
('Gluten Free', 'Does not contain gluten'),
('Dairy Free', 'Does not contain dairy'),
('Popular', 'Customer favorite dishes'),
('New', 'Recently added dishes'),
('Seasonal', 'Seasonal special dishes');
