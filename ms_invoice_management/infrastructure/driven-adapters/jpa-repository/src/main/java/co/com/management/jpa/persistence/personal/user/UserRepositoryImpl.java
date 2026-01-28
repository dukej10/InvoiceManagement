package co.com.management.jpa.persistence.personal.user;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.model.user.User;
import co.com.management.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl extends AdapterOperations<User, UserDao, String, UserDaoRepository>
implements UserRepository {

    private final RolesDaoRepository rolesDaoRepository;

    protected UserRepositoryImpl(UserDaoRepository repository,
                                 RolesDaoRepository rolesDaoRepository,
                                 ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.rolesDaoRepository = rolesDaoRepository;
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

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        Set<RolesDao> roles = user.getRoles().stream()
                .map(rolesDaoRepository::findByName)
                .collect(java.util.stream.Collectors.toSet());

        UserDao dao = new UserDao();
        dao.setUsername(user.getUsername());
        dao.setPassword(user.getPassword());
        dao.setEnabled(Boolean.TRUE);
        dao.setRoles(roles);

        UserDao savedDao = repository.save(dao);

        User saved = mapper.map(savedDao, User.class);
        saved.setRoles(savedDao.getRoles().stream().map(RolesDao::getName).toList());
        return saved;
    }
}
