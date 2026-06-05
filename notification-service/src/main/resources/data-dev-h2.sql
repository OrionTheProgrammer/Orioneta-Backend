INSERT INTO notifications (id, user_id, type, title, body, read, created_at, read_at)
VALUES
    ('30000000-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222', 'MESSAGE_SENT', 'Nuevo mensaje', 'Tienes un mensaje nuevo en Orioneta.', FALSE, TIMESTAMP '2026-06-05 09:12:00', NULL),
    ('30000000-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'FRIEND_REQUEST', 'Solicitud de amistad', 'Un usuario te envio una solicitud de amistad.', TRUE, TIMESTAMP '2026-06-05 09:20:00', TIMESTAMP '2026-06-05 09:21:00');
