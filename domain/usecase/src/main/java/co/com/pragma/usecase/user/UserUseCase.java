package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    public Mono<User> registrarUsuario(User usuario) {
        // Campos obligatorios no nulos o vacíos.
        if (usuario.getNombres() == null || usuario.getNombres().isBlank() ||
                usuario.getApellidos() == null || usuario.getApellidos().isBlank() ||
                usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().isBlank() ||
                usuario.getSalarioBase() == null) {
            return Mono.error(new IllegalArgumentException("Nombres, apellidos, correo y salario son obligatorios."));
        }

        // Formato correcto de los datos.
        if (!usuario.getCorreoElectronico().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return Mono.error(new IllegalArgumentException("Formato de correo electrónico inválido."));
        }
        if (usuario.getSalarioBase() < 0 || usuario.getSalarioBase() > 15000000) {
            return Mono.error(new IllegalArgumentException("El salario base debe estar entre 0 y 15,000,000."));
        }

        // Correo no registrado previamente.
        return userRepository.existeCorreo(usuario.getCorreoElectronico())
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return Mono.error(new IllegalStateException("El correo electrónico ya está registrado."));
                    }
                    return userRepository.guardarUsuario(usuario);
                });
    }
}
