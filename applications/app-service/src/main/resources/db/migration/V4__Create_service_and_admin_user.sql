-- Usuario de Servicio para comunicación interna
INSERT INTO usuarios
(documento_identidad, nombres, apellidos, correo_electronico, password, rol, salario_base)
VALUES
    (
        'SERVICE_ACCOUNT_01',
        'Servicio',
        'Crediya',
        'reportes@crediva.internal',
        '$2a$12$.T29tHsHMe1RZfOB2nKUfuJb/cw5dSFGTy6x3idc12b8v/f/iENE2',
        'SERVICE',
        0
    )
    ON CONFLICT (documento_identidad) DO NOTHING;

-- Usuario Admin para enviar correos
INSERT INTO usuarios (documento_identidad, nombres, apellidos, fecha_nacimiento, direccion, telefono, correo_electronico, password, rol, salario_base)
VALUES ('1006', 'Andres', 'Camargo', '2002-11-18', 'Oficina Central', '1111111', 'andrescamargooaws@gmail.com', '$2a$12$rC.6Xyo/RNKYw9/QaxnLiOyEi.NwEL7fjvSmH3Ze8oXN0HPwL/EFi', 'ADMIN', 10000000)
    ON CONFLICT (documento_identidad) DO NOTHING;
