-- Create transactions table
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    transaction_reference VARCHAR(100) NOT NULL UNIQUE,
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('CREDIT_CARD', 'BANK_TRANSFER', 'SINPE_MOVIL')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'CRC',
    provider_response TEXT,
    proof_of_payment_url VARCHAR(500),
    processed_at TIMESTAMP,
    failure_reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE RESTRICT
);

-- Create indexes for better query performance
CREATE INDEX idx_transactions_order_id ON transactions(order_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_payment_method ON transactions(payment_method);
CREATE INDEX idx_transactions_transaction_reference ON transactions(transaction_reference);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
