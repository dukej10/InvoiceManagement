package co.com.management.jpa.persistence.client;

import co.com.management.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientDaoRepository extends JpaRepository<ClientDao, UUID> {
    Optional<ClientDao> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);
}
