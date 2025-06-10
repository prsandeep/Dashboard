# -- Insert roles if they don't exist
# INSERT INTO roles (name)
# SELECT 'ROLE_USER'
#     WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');
#
# INSERT INTO roles (name)
# SELECT 'ROLE_ADMIN'
#     WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');
#
# -- Insert default admin user if not exists (password: admin123)
# -- BCrypt encoded password: $2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi
# DO $$
# BEGIN
#     IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin') THEN
#         INSERT INTO users (username, email, password)
#         VALUES ('admin', 'admin@example.com', '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi');
#
# INSERT INTO user_roles (user_id, role_id)
# VALUES (
#            (SELECT id FROM users WHERE username = 'admin'),
#            (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
#        );
# END IF;
# END
# $$;