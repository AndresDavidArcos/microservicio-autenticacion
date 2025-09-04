package co.com.pragma.model.user;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String id;
    private String documentoIdentidad;
    private String nombres;
    private String apellidos;
    private java.time.LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private String password;
    private Double salarioBase;
    private String rol;
}
