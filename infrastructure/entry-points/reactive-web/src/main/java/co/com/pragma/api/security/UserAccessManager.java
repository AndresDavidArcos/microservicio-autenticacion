package co.com.pragma.api.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication.map(auth -> {
            String documentoFromPath = context.getExchange().getRequest().getURI().getPath()
                    .replaceAll(".*/", "");

            String authenticatedDocumento = auth.getName();

            boolean isAdminOrAsesor = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                    grantedAuthority.getAuthority().equals("ROLE_ASESOR")
                    );

            boolean isAllowed = isAdminOrAsesor || authenticatedDocumento.equals(documentoFromPath);

            return new AuthorizationDecision(isAllowed);
        });
    }
}
