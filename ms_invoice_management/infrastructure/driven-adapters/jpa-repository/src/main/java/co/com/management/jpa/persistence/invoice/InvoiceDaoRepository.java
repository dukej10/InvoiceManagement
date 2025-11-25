package co.com.management.jpa.persistence.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceDaoRepository extends JpaRepository<InvoiceDao, String>
{
    Page<InvoiceDao> findAllPageableByClientId(String clientId, Pageable pageable);
    Optional<InvoiceDao> findByClientId(String clientId);
    List<InvoiceDao> findAllByClientId(String clientId);
}
