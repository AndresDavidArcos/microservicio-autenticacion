package co.com.pragma.model.user.gateways;
import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> guardarUsuario(User usuario);
    Mono<User> buscarPorCorreo(String correo);
    Mono<User> buscarPorDocumento(String documentoIdentidad);
    Mono<Boolean> existePorDocumento(String documentoIdentidad);
    Mono<Boolean> existeCorreo(String correo);
}
