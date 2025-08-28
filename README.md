### Microservicio de Autenticación
**URL Base:** `http://localhost:8080`

#### 1. Registrar un nuevo usuario
Registra un nuevo solicitante en el sistema con sus datos personales.

* **Endpoint:** `POST /api/v1/usuarios`
* **Método:** `POST`

**Parámetros del Body (Request Body)**

Se debe enviar un objeto JSON con la siguiente estructura:
```json
{
    "documentoIdentidad": "123456789",
    "nombres": "Juan Alberto",
    "apellidos": "Perez Lopez",
    "fechaNacimiento": "1990-05-15",
    "direccion": "Calle Falsa 123",
    "telefono": "3001234567",
    "correoElectronico": "juan.perez@email.com",
    "salarioBase": 5000000
}
```
| Campo | Tipo | Descripción | Obligatorio |
| :--- | :--- | :--- | :--- |
| `documentoIdentidad` | String | Número de identificación único del solicitante. | Sí |
| `nombres` | String | Nombres del solicitante. | Sí |
| `apellidos` | String | Apellidos del solicitante. | Sí |
| `fechaNacimiento` | String | Fecha de nacimiento en formato `YYYY-MM-DD`. | No |
| `direccion` | String | Dirección de residencia. | No |
| `telefono` | String | Número de teléfono de contacto. | No |
| `correoElectronico` | String | Correo electrónico válido. Debe ser único. | Sí |
| `salarioBase` | Number | Salario base mensual (entre 0 y 15,000,000). | Sí |

**Posibles Salidas (Responses)**

* **`201 Created`**: El usuario fue registrado exitosamente.
    * **Cuerpo:** Un objeto JSON con los datos del usuario creado.
* **`400 Bad Request`**: Los datos enviados son inválidos.
    * **Cuerpo:** Un mensaje de error describiendo la validación que falló (ej: "Nombres, apellidos, correo y salario son obligatorios.", "Formato de correo electrónico inválido.").
* **`409 Conflict`**: El `documentoIdentidad` o el `correoElectronico` ya se encuentran registrados en el sistema.
    * **Cuerpo:** Un mensaje de error (ej: "El correo electrónico ya está registrado.").

---

#### 2. Verificar existencia de un usuario
Verifica si un usuario existe en el sistema a través de su número de documento.

* **Endpoint:** `HEAD /api/v1/usuarios/existe/{documento}`
* **Método:** `HEAD`

**Parámetros de Ruta (Path Parameters)**

| Parámetro | Tipo | Descripción |
| :--- | :--- | :--- |
| `{documento}` | String | El número de documento de identidad a verificar. |

**Posibles Salidas (Responses)**

* **`200 OK`**: El usuario con el documento especificado sí existe.
    * **Cuerpo:** Vacío.
* **`404 Not Found`**: No se encontró ningún usuario con el documento especificado.
    * **Cuerpo:** Vacío.
