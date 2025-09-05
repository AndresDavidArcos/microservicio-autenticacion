package co.com.pragma.api;

import co.com.pragma.api.dto.LoginDTO;
import co.com.pragma.api.dto.LoginResponseDTO;
import co.com.pragma.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "registrarUsuario",
                    operation = @Operation(
                            summary = "Registrar un nuevo usuario",
                            description = "Crea un nuevo usuario en el sistema. Requiere rol de ADMIN o ASESOR.",
                            operationId = "registrarUsuario",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos del usuario a registrar",
                                    content = @Content(schema = @Schema(implementation = UserDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                                    @ApiResponse(responseCode = "403", description = "Acceso denegado (rol no permitido)"),
                                    @ApiResponse(responseCode = "409", description = "Conflicto, el correo o documento ya existen")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            summary = "Iniciar sesión",
                            description = "Autentica a un usuario con su correo y contraseña y devuelve un token JWT.",
                            operationId = "login",
                            tags = {"Autenticación"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Credenciales de acceso",
                                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                                    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/existe/{documento}",
                    method = RequestMethod.HEAD,
                    beanClass = Handler.class,
                    beanMethod = "existeUsuarioPorDocumento",
                    operation = @Operation(
                            summary = "Verificar existencia de un usuario",
                            description = "Verifica si un usuario existe a partir de su número de documento.",
                            operationId = "existeUsuarioPorDocumento",
                            tags = {"Usuarios"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "documento", description = "Número de documento del usuario", required = true, example = "123456789")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "El usuario existe"),
                                    @ApiResponse(responseCode = "404", description = "El usuario no existe")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{documento}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "buscarUsuarioPorDocumento",
                    operation = @Operation(
                            summary = "Obtener datos de un usuario por documento",
                            description = "Recupera la información completa de un usuario. Requiere rol de ADMIN o ASESOR.",
                            operationId = "buscarUsuarioPorDocumento",
                            tags = {"Usuarios"},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "documento", description = "Número de documento del usuario", required = true, example = "123456789")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                                    @ApiResponse(responseCode = "403", description = "Acceso denegado (rol no permitido)"),
                                    @ApiResponse(responseCode = "404", description = "El usuario no existe")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::registrarUsuario)
                .andRoute(POST("/api/v1/login"), handler::login)
                .andRoute(HEAD("/api/v1/usuarios/existe/{documento}"), handler::existeUsuarioPorDocumento)
                .andRoute(GET("/api/v1/usuarios/{documento}"), handler::buscarUsuarioPorDocumento);
    }
}
