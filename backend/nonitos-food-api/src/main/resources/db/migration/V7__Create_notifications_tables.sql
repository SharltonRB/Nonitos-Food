-- Create notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(30) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    related_entity_id BIGINT,
    related_entity_type VARCHAR(50),
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create notification templates table
CREATE TABLE notification_templates (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(30) NOT NULL,
    title_template VARCHAR(200) NOT NULL,
    message_template TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_type ON notifications(type);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- Insert default notification templates
INSERT INTO notification_templates (code, type, title_template, message_template) VALUES
('ORDER_CREATED', 'ORDER_CREATED', 'Order Created', 'Your order {{orderCode}} has been created successfully. Total: {{totalAmount}}'),
('ORDER_PAID', 'ORDER_PAID', 'Payment Confirmed', 'Payment for order {{orderCode}} has been confirmed. Your order is now being processed.'),
('ORDER_IN_PREPARATION', 'ORDER_IN_PREPARATION', 'Order in Preparation', 'Your order {{orderCode}} is now being prepared.'),
('ORDER_READY', 'ORDER_READY', 'Order Ready for Pickup', 'Your order {{orderCode}} is ready for pickup! Please use your QR code.'),
('ORDER_COMPLETED', 'ORDER_COMPLETED', 'Order Completed', 'Thank you! Your order {{orderCode}} has been completed.'),
('ORDER_CANCELLED', 'ORDER_CANCELLED', 'Order Cancelled', 'Your order {{orderCode}} has been cancelled. Reason: {{reason}}'),
('PAYMENT_RECEIVED', 'PAYMENT_RECEIVED', 'Payment Received', 'We have received your payment for order {{orderCode}}. Awaiting verification.'),
('PAYMENT_FAILED', 'PAYMENT_FAILED', 'Payment Failed', 'Payment for order {{orderCode}} failed. Please try again.'),
('MENU_PUBLISHED', 'MENU_PUBLISHED', 'New Menu Available', 'A new weekly menu is now available! Check it out and place your order.'),
('SYSTEM_ANNOUNCEMENT', 'SYSTEM_ANNOUNCEMENT', 'Announcement', '{{message}}');
