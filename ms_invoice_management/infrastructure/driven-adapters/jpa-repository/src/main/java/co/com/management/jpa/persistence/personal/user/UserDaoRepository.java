package co.com.management.jpa.persistence.personal.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDaoRepository extends JpaRepository<UserDao, String> {

    @Query("""
    select distinct u from UserDao u
    left join fetch u.roles
    where u.username = :username
  """)
    Optional<UserDao> findByUsernameWithRoles(@Param("username") String username);

    boolean existsByUsername(String username);
}
