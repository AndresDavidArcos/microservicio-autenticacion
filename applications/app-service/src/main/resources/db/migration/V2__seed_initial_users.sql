-- Usuario Admin
INSERT INTO usuarios (documento_identidad, nombres, apellidos, fecha_nacimiento, direccion, telefono, correo_electronico, password, rol, salario_base)
VALUES ('100', 'Admin', 'Principal', '1990-01-01', 'Oficina Central', '1111111', 'admin@crediva.com', '$2a$12$Qz5dQYtC0N9FbNpBmr5jVuVWigPU6TbPxojXkahzh2QRkt667WLxm', 'ADMIN', 10000000)
    ON CONFLICT (documento_identidad) DO NOTHING;

-- Usuario Asesor
INSERT INTO usuarios (documento_identidad, nombres, apellidos, fecha_nacimiento, direccion, telefono, correo_electronico, password, rol, salario_base)
VALUES ('1020304050', 'Carlos', 'Santana', '1985-03-10', 'Oficina Principal', '2222222', 'carlos.santana@email.com', '$2a$12$uwZsBJtXQjNRU3HV62gf9ebd8sYXVfJTpoFfADxz2mZ.Z72Gr7.tm', 'ASESOR', 8000000)
    ON CONFLICT (documento_identidad) DO NOTHING;

-- Usuario Cliente 1
INSERT INTO usuarios (documento_identidad, nombres, apellidos, fecha_nacimiento, direccion, telefono, correo_electronico, password, rol, salario_base)
VALUES ('200', 'Carla', 'Cliente', '1995-05-05', 'Calle Cliente 123', '3333333', 'carla.cliente@email.com', '$2a$10$ROmZ25OhKQLXCU19/XX/eOacqlofLPmcO5Y7UyzFPFyL0boW24zI6', 'CLIENTE', 3000000)
    ON CONFLICT (documento_identidad) DO NOTHING;

-- Usuario Cliente 2
INSERT INTO usuarios (documento_identidad, nombres, apellidos, fecha_nacimiento, direccion, telefono, correo_electronico, password, rol, salario_base)
VALUES ('1122334455', 'Ana', 'García', '1992-07-22', 'Avenida Prueba 456', '4444444', 'ana.garcia@email.com', '$2a$10$ROmZ25OhKQLXCU19/XX/eOacqlofLPmcO5Y7UyzFPFyL0boW24zI6', 'CLIENTE', 7500000)
    ON CONFLICT (documento_identidad) DO NOTHING;
