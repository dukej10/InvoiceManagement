package co.com.management.usecase.login;

import co.com.management.model.user.User;
import co.com.management.model.user.gateways.PasswordEncoderGateway;
import co.com.management.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoderGateway passwordEncoder;

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }
}
