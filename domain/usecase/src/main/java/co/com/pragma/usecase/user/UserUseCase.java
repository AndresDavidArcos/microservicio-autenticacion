package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> registrarUsuario(User user) {

        if (user.getDocumentoIdentidad() == null || user.getDocumentoIdentidad().isBlank() ||
                user.getNombres() == null || user.getNombres().isBlank() ||
                user.getApellidos() == null || user.getApellidos().isBlank() ||
                user.getCorreoElectronico() == null || user.getCorreoElectronico().isBlank() ||
                user.getSalarioBase() == null) {
            return Mono.error(new IllegalArgumentException("Documento, nombres, apellidos, correo y salario son obligatorios."));
        }

        if (!user.getCorreoElectronico().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return Mono.error(new IllegalArgumentException("Formato de correo electrónico inválido."));
        }
        if (user.getSalarioBase() < 0 || user.getSalarioBase() > 15000000) {
            return Mono.error(new IllegalArgumentException("El salario base debe estar entre 0 y 15,000,000."));
        }

        return Mono.zip(
                userRepository.existeCorreo(user.getCorreoElectronico()),
                userRepository.existePorDocumento(user.getDocumentoIdentidad())
        ).flatMap(tuple -> {
            boolean correoExiste = tuple.getT1();
            boolean documentoExiste = tuple.getT2();

            if (correoExiste) {
                return Mono.error(new IllegalStateException("El correo electrónico ya está registrado."));
            }
            if (documentoExiste) {
                return Mono.error(new IllegalStateException("El documento de identidad ya está registrado."));
            }

            return userRepository.guardarUsuario(user);
        });
    }

    public Mono<Boolean> existePorDocumento(String documentoIdentidad) {
        return userRepository.existePorDocumento(documentoIdentidad);
    }
}
