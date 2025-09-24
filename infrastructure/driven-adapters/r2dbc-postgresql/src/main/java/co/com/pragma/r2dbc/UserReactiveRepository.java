package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByCorreoElectronico(String correo);
    Mono<UserEntity> findByDocumentoIdentidad(String documentoIdentidad);
    Mono<Boolean> existsByCorreoElectronico(String correo);
    Mono<Boolean> existsByDocumentoIdentidad(String documentoIdentidad);
    Flux<UserEntity> findByRol(String rol);
}
