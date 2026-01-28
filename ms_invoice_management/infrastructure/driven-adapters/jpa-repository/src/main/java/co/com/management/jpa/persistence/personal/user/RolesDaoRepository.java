package co.com.management.jpa.persistence.personal.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesDaoRepository extends JpaRepository<RolesDao, String> {
    RolesDao findByName(String name);
}
