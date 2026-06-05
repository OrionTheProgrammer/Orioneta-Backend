INSERT INTO audit_events (id, source_service, action, target_type, target_id, actor_user_id, detail, occurred_at)
VALUES
    ('80000000-0000-0000-0000-000000000001', 'conversation-service', 'GROUP_CREATED', 'CONVERSATION', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'Grupo Equipo Orioneta creado en datos dev-h2.', TIMESTAMP '2026-06-05 09:05:00'),
    ('80000000-0000-0000-0000-000000000002', 'neta-market-service', 'TEMPLATE_APPROVED', 'NETA_TEMPLATE', '60000000-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', 'Template Neta Night aprobado en datos dev-h2.', TIMESTAMP '2026-06-05 09:08:00');
