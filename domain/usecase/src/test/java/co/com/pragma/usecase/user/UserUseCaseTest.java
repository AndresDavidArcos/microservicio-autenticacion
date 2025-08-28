package co.com.pragma.usecase.user;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .documentoIdentidad("123456789")
                .nombres("John")
                .apellidos("Doe")
                .correoElectronico("john.doe@email.com")
                .salarioBase(5000000.0)
                .build();
    }

    @Test
    @DisplayName("Prueba de registro exitoso de un usuario")
    void registrarUsuarioExitoso() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(false));
        when(userRepository.guardarUsuario(any(User.class))).thenReturn(Mono.just(user));

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("Prueba de fallo al registrar por correo duplicado")
    void registrarUsuarioFalloCorreoDuplicado() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(true));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(false));

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de fallo al registrar por documento duplicado")
    void registrarUsuarioFalloDocumentoDuplicado() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(true));

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de fallo por campos obligatorios nulos")
    void registrarUsuarioFalloCamposNulos() {
        user.setNombres(null);

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de fallo por formato de correo inválido")
    void registrarUsuarioFalloFormatoCorreo() {
        user.setCorreoElectronico("correo-invalido");

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
