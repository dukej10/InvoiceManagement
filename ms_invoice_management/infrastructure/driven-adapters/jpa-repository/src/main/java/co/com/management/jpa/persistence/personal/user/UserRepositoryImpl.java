package co.com.management.jpa.persistence.personal.user;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.model.user.User;
import co.com.management.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AdapterOperations<User, UserDao, String, UserDaoRepository>
implements UserRepository {
    protected UserRepositoryImpl(UserDaoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> {
            return mapper.map(d, User.class);
        });
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameWithRoles(username)
                .filter(u -> Boolean.TRUE.equals(u.getEnabled()))
                .map(userDao -> {
                    User user = mapper.map(userDao, User.class);
                    user.setRoles(userDao.getRoles().stream()
                            .map(RolesDao::getName)
                            .toList());
                    return user;
                });
    }
}
