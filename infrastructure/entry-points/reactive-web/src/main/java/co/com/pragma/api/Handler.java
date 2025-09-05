package co.com.pragma.api;


import co.com.pragma.api.dto.LoginDTO;
import co.com.pragma.api.dto.LoginResponseDTO;
import co.com.pragma.api.dto.UserDTO;
import co.com.pragma.api.mapper.UserDTOMapper;
import co.com.pragma.api.validation.ValidatorHandler;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> registrarUsuario(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(validatorHandler::validate)
                .map(userDTOMapper::toModel)
                .flatMap(userUseCase::registrarUsuario)
                .map(userDTOMapper::toDTO)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDTO.class)
                .flatMap(validatorHandler::validate)
                .flatMap(dto -> userUseCase.login(dto.getCorreo(), dto.getPassword()))
                .flatMap(token -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new LoginResponseDTO(token)));
    }

    public Mono<ServerResponse> existeUsuarioPorDocumento(ServerRequest serverRequest) {
        String documento = serverRequest.pathVariable("documento");
        return userUseCase.existePorDocumento(documento)
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return ServerResponse.ok().build();
                    }
                    return ServerResponse.notFound().build();
                });
    }

    public Mono<ServerResponse> buscarUsuarioPorDocumento(ServerRequest serverRequest) {
        String documento = serverRequest.pathVariable("documento");
        return userUseCase.buscarPorDocumento(documento)
                .map(userDTOMapper::toDTO)
                .flatMap(userDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userDTO))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
