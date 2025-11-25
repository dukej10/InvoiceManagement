package co.com.management.jpa.persistence.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDaoRepository extends JpaRepository<ClientDao, String> {
    Optional<ClientDao> findByDocumentNumberAndDocumentType(String documentNumber, String documentType);
}
