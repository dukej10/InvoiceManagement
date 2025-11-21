package co.com.management.jpa.persistence.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceDaoRepository extends JpaRepository<InvoiceDao, String>
{
    Page<InvoiceDao> findAllByClientId(String clientId, Pageable pageable);
    Optional<InvoiceDao> findByClientId(String clientId);
}
