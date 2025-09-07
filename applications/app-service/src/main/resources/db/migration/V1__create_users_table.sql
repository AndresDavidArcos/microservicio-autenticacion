CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          documento_identidad VARCHAR(255) NOT NULL,
                          nombres VARCHAR(255) NOT NULL,
                          apellidos VARCHAR(255) NOT NULL,
                          fecha_nacimiento DATE,
                          direccion VARCHAR(255),
                          telefono VARCHAR(50),
                          correo_electronico VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          rol VARCHAR(50) NOT NULL,
                          salario_base NUMERIC(15, 2) NOT NULL,
                          CONSTRAINT usuarios_documento_identidad_key UNIQUE (documento_identidad),
                          CONSTRAINT usuarios_correo_electronico_key UNIQUE (correo_electronico)
);
