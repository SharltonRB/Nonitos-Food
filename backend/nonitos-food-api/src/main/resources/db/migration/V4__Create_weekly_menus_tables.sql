-- Create weekly menus table
CREATE TABLE weekly_menus (
    id BIGSERIAL PRIMARY KEY,
    week_start_date DATE NOT NULL UNIQUE,
    week_end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
    total_calories INTEGER NOT NULL DEFAULT 0,
    total_protein INTEGER NOT NULL DEFAULT 0,
    total_carbs INTEGER NOT NULL DEFAULT 0,
    total_fats INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create menu days table
CREATE TABLE menu_days (
    id BIGSERIAL PRIMARY KEY,
    weekly_menu_id BIGINT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    meal_type VARCHAR(20) NOT NULL CHECK (meal_type IN ('BREAKFAST', 'LUNCH', 'DINNER')),
    dish_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_menu_day_weekly_menu FOREIGN KEY (weekly_menu_id) REFERENCES weekly_menus(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_day_dish FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE RESTRICT,
    CONSTRAINT uk_menu_day UNIQUE (weekly_menu_id, day_of_week, meal_type)
);

-- Create indexes for better query performance
CREATE INDEX idx_weekly_menus_status ON weekly_menus(status);
CREATE INDEX idx_weekly_menus_week_start_date ON weekly_menus(week_start_date);
CREATE INDEX idx_menu_days_weekly_menu_id ON menu_days(weekly_menu_id);
CREATE INDEX idx_menu_days_day_of_week ON menu_days(day_of_week);
CREATE INDEX idx_menu_days_dish_id ON menu_days(dish_id);
