package co.com.pragma.usecase.user;

import co.com.pragma.model.exception.ConflictException;
import co.com.pragma.model.exception.UnauthorizedException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.gateways.PasswordManager;
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
    @Mock
    private PasswordManager passwordManager;

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
                .password("Password123")
                .rol("CLIENTE")
                .salarioBase(5000000.0)
                .build();
    }

    @Test
    @DisplayName("Prueba de registro exitoso de un usuario")
    void registrarUsuarioExitoso() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(false));
        when(passwordManager.encode(anyString())).thenReturn("password-encriptado");
        when(userRepository.guardarUsuario(any(User.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectNextMatches(u -> u.getPassword().equals("password-encriptado"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Prueba de fallo al registrar por correo duplicado")
    void registrarUsuarioFalloCorreoDuplicado() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(true));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(false));

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectError(ConflictException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de fallo al registrar por documento duplicado")
    void registrarUsuarioFalloDocumentoDuplicado() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(true));

        Mono<User> resultado = userUseCase.registrarUsuario(user);

        StepVerifier.create(resultado)
                .expectError(ConflictException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de login exitoso")
    void loginExitoso() {
        User userFromDb = user.toBuilder().password("password-encriptado").build();
        when(userRepository.buscarPorCorreo(anyString())).thenReturn(Mono.just(userFromDb));
        when(passwordManager.matches("Password123", "password-encriptado")).thenReturn(true);

        Mono<User> resultado = userUseCase.login("john.doe@email.com", "Password123");

        StepVerifier.create(resultado)
                .expectNext(userFromDb)
                .verifyComplete();
    }

    @Test
    @DisplayName("Prueba de login fallido por usuario no encontrado")
    void loginFalloUsuarioNoEncontrado() {
        when(userRepository.buscarPorCorreo(anyString())).thenReturn(Mono.empty());

        Mono<User> resultado = userUseCase.login("noexiste@email.com", "Password123");

        StepVerifier.create(resultado)
                .expectError(UnauthorizedException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de login fallido por contraseña incorrecta")
    void loginFalloPasswordIncorrecta() {
        User userFromDb = user.toBuilder().password("password-encriptado").build();
        when(userRepository.buscarPorCorreo(anyString())).thenReturn(Mono.just(userFromDb));
        when(passwordManager.matches("password-incorrecto", "password-encriptado")).thenReturn(false);

        Mono<User> resultado = userUseCase.login("john.doe@email.com", "password-incorrecto");

        StepVerifier.create(resultado)
                .expectError(UnauthorizedException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de búsqueda de usuario por documento exitosa")
    void buscarPorDocumentoExitoso() {
        when(userRepository.buscarPorDocumento(anyString())).thenReturn(Mono.just(user));

        Mono<User> resultado = userUseCase.buscarPorDocumento("123456789");

        StepVerifier.create(resultado)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("Prueba de verificación de existencia por documento")
    void existePorDocumentoExitoso() {
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> resultado = userUseCase.existePorDocumento("123456789");

        StepVerifier.create(resultado)
                .expectNext(true)
                .verifyComplete();
    }
}
