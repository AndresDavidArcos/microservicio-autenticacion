package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;

public interface AuthTokenGenerator {
    String generateToken(User user);
}
