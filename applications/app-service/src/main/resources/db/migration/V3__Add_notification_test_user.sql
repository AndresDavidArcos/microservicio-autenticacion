-- Usuario Cliente de Prueba para Notificaciones
INSERT INTO usuarios
(documento_identidad, nombres, apellidos, fecha_nacimiento, direccion, telefono, correo_electronico, password, rol, salario_base)
VALUES
    (
        '300',
        'Andres',
        'Titauro',
        '1999-01-01',
        'Calle de las Pruebas 789',
        '5555555',
        'andretitauro@gmail.com',
        '$2a$12$ZBu/25zvZxFhet.RYH4jf.Q.mYhbUHg.jxJgPo7/1UeUD5Y8FzpQy',
        'CLIENTE',
        2500000
    )
    ON CONFLICT (documento_identidad) DO NOTHING;
