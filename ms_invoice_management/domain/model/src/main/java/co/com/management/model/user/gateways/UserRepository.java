package co.com.management.model.user.gateways;

import co.com.management.model.user.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);
}

