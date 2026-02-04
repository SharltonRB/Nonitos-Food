-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_code VARCHAR(10) NOT NULL UNIQUE,
    client_id BIGINT NOT NULL,
    weekly_menu_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING_PAYMENT', 'PAID', 'IN_PREPARATION', 'READY_FOR_PICKUP', 'COMPLETED', 'CANCELLED')),
    total_amount DECIMAL(10, 2) NOT NULL,
    meals_per_day INTEGER NOT NULL CHECK (meals_per_day BETWEEN 1 AND 3),
    include_breakfast BOOLEAN NOT NULL DEFAULT false,
    include_lunch BOOLEAN NOT NULL DEFAULT false,
    include_dinner BOOLEAN NOT NULL DEFAULT false,
    pickup_date_time TIMESTAMP NOT NULL,
    qr_code VARCHAR(500),
    special_instructions TEXT,
    cancellation_reason VARCHAR(500),
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_client FOREIGN KEY (client_id) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_order_weekly_menu FOREIGN KEY (weekly_menu_id) REFERENCES weekly_menus(id) ON DELETE RESTRICT
);

-- Create order status history table
CREATE TABLE order_status_history (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    previous_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    changed_by_user_id BIGINT,
    notes VARCHAR(500),
    changed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_status_history_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_status_history_user FOREIGN KEY (changed_by_user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_orders_client_id ON orders(client_id);
CREATE INDEX idx_orders_weekly_menu_id ON orders(weekly_menu_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_order_code ON orders(order_code);
CREATE INDEX idx_orders_pickup_date_time ON orders(pickup_date_time);
CREATE INDEX idx_order_status_history_order_id ON order_status_history(order_id);
CREATE INDEX idx_order_status_history_changed_at ON order_status_history(changed_at);
