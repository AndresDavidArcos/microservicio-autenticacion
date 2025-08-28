package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
        > implements UserRepository {

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> guardarUsuario(User user) {
        return this.save(user);
    }

    @Override
    public Mono<User> buscarPorCorreo(String correo) {
        return repository.findByCorreoElectronico(correo)
                .map(userEntity -> this.toEntity(userEntity));
    }

    @Override
    public Mono<Boolean> existeCorreo(String correo) {
        return repository.existsByCorreoElectronico(correo);
    }
}
