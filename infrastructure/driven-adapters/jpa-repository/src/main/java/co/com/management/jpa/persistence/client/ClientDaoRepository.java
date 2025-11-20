package co.com.management.jpa.persistence.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientDaoRepository extends JpaRepository<ClientDao, UUID> {
}
