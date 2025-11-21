package co.com.management.jpa.persistence.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface InvoiceDaoRepository extends JpaRepository<InvoiceDao, String>
{
    Page<InvoiceDao> findAllByClientId(String clientId, Pageable pageable);
}
