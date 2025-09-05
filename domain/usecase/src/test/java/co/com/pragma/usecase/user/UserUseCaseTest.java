package co.com.pragma.usecase.user;

import co.com.pragma.model.exception.ConflictException;
import co.com.pragma.model.exception.UnauthorizedException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.AuthTokenGenerator;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthTokenGenerator tokenGenerator;

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
                .salarioBase(5000000.0)
                .build();
    }

    @Test
    @DisplayName("Prueba de registro exitoso de un usuario")
    void registrarUsuarioExitoso() {
        when(userRepository.existeCorreo(anyString())).thenReturn(Mono.just(false));
        when(userRepository.existePorDocumento(anyString())).thenReturn(Mono.just(false));
        when(passwordEncoder.encode(anyString())).thenReturn("password-encriptado");
        when(userRepository.guardarUsuario(any(User.class))).thenAnswer(invocation -> {
            User userArgument = invocation.getArgument(0);
            userArgument.setPassword("password-encriptado");
            return Mono.just(userArgument);
        });

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
    @DisplayName("Prueba de login exitoso")
    void loginExitoso() {
        String tokenEsperado = "jwt-token-de-prueba";
        user.setPassword("password-encriptado");

        when(userRepository.buscarPorCorreo(anyString())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("Password123", "password-encriptado")).thenReturn(true);
        when(tokenGenerator.generateToken(any(User.class))).thenReturn(tokenEsperado);

        Mono<String> resultado = userUseCase.login("john.doe@email.com", "Password123");

        StepVerifier.create(resultado)
                .expectNext(tokenEsperado)
                .verifyComplete();
    }

    @Test
    @DisplayName("Prueba de login fallido por usuario no encontrado")
    void loginFalloUsuarioNoEncontrado() {
        when(userRepository.buscarPorCorreo(anyString())).thenReturn(Mono.empty());

        Mono<String> resultado = userUseCase.login("noexiste@email.com", "Password123");

        StepVerifier.create(resultado)
                .expectError(UnauthorizedException.class)
                .verify();
    }

    @Test
    @DisplayName("Prueba de login fallido por contraseña incorrecta")
    void loginFalloPasswordIncorrecta() {
        user.setPassword("password-encriptado");

        when(userRepository.buscarPorCorreo(anyString())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password-incorrecto", "password-encriptado")).thenReturn(false);

        Mono<String> resultado = userUseCase.login("john.doe@email.com", "password-incorrecto");

        StepVerifier.create(resultado)
                .expectError(UnauthorizedException.class)
                .verify();
    }
}
