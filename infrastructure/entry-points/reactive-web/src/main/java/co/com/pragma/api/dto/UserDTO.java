package co.com.pragma.api.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    @NotBlank(message = "El documento de identidad es obligatorio.")
    private String documentoIdentidad;

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellidos;

    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    private String correoElectronico;

    @NotNull(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @NotNull(message = "El salario base es obligatorio.")
    @Min(value = 0, message = "El salario base no puede ser negativo.")
    @Max(value = 15000000, message = "El salario base no puede exceder los 15,000,000.")
    private Double salarioBase;

    private String rol;
}
