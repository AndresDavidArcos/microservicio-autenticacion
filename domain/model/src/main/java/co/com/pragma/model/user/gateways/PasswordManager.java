package co.com.pragma.model.user.gateways;

public interface PasswordManager {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
