package co.com.pragma.usecase.user;

import co.com.pragma.model.exception.ConflictException;
import co.com.pragma.model.exception.UnauthorizedException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.gateways.PasswordManager;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public Mono<User> registrarUsuario(User user) {
        return validarExistencia(user)
                .flatMap(validatedUser -> {
                    validatedUser.setPassword(passwordManager.encode(validatedUser.getPassword()));
                    return userRepository.guardarUsuario(validatedUser);
                });
    }

    public Mono<User> login(String correo, String password) {
        return userRepository.buscarPorCorreo(correo)
                .switchIfEmpty(Mono.error(new UnauthorizedException("Credenciales inválidas")))
                .flatMap(user -> {
                    if (passwordManager.matches(password, user.getPassword())) {
                        return Mono.just(user);
                    }
                    return Mono.error(new UnauthorizedException("Credenciales inválidas"));
                });
    }

    public Mono<Boolean> existePorDocumento(String documentoIdentidad) {
        return userRepository.existePorDocumento(documentoIdentidad);
    }

    public Mono<User> buscarPorDocumento(String documentoIdentidad) {
        return userRepository.buscarPorDocumento(documentoIdentidad);
    }

    private Mono<User> validarExistencia(User user) {
        return Mono.zip(
                userRepository.existeCorreo(user.getCorreoElectronico()),
                userRepository.existePorDocumento(user.getDocumentoIdentidad())
        ).flatMap(tuple -> {
            boolean correoExiste = tuple.getT1();
            boolean documentoExiste = tuple.getT2();
            if (correoExiste) {
                return Mono.error(new ConflictException("El correo electrónico ya está registrado."));
            }
            if (documentoExiste) {
                return Mono.error(new ConflictException("El documento de identidad ya está registrado."));
            }
            return Mono.just(user);
        });
    }
}
