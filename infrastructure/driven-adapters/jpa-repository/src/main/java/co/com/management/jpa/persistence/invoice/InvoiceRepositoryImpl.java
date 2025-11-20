package co.com.management.jpa.persistence.invoice;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.jpa.persistence.client.ClientDao;
import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class InvoiceRepositoryImpl extends AdapterOperations<Invoice, InvoiceDao, UUID, InvoiceDaoRepository>
        implements InvoiceRepository {

    public InvoiceRepositoryImpl(InvoiceDaoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> {
            return mapper.map(d, Invoice.class);
        });
    }

    @Override
    public List<Invoice> getAll() {
        return List.of();
    }

    @Override
    public PageResult<Invoice> getAllByIdCliente(UUID id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDao> invoicesFound = repository.findAllByIdClient(id, pageable);
        List<Invoice> invoices = invoicesFound.getContent().stream()
                .map(this::toEntity)
                .toList();
        return new PageResult<>(
                invoices,
                invoicesFound.getNumber(),
                invoicesFound.getSize(),
                invoicesFound.getTotalElements(),
                invoicesFound.getTotalPages(),
                invoicesFound.hasNext(),
                invoicesFound.hasPrevious()
        );    }

    @Override
    public PageResult<Invoice> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDao> invoicesFound = repository.findAll(pageable);
        List<Invoice> invoices = invoicesFound.getContent().stream()
                .map(this::toEntity)
                .toList();
        return new PageResult<>(
                invoices,
                invoicesFound.getNumber(),
                invoicesFound.getSize(),
                invoicesFound.getTotalElements(),
                invoicesFound.getTotalPages(),
                invoicesFound.hasNext(),
                invoicesFound.hasPrevious()
        );    }
    }

    @Override
    public Invoice registerInvoice(Invoice invoice) {
        return null;
    }
}
