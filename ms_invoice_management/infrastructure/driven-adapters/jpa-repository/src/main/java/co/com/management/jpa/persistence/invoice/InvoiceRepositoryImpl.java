package co.com.management.jpa.persistence.invoice;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.jpa.helper.Util;
import co.com.management.model.PageResult;
import co.com.management.model.ProductInfo;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class InvoiceRepositoryImpl extends AdapterOperations<Invoice, InvoiceDao, String, InvoiceDaoRepository>
        implements InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;



    public InvoiceRepositoryImpl(InvoiceDaoRepository repository, ObjectMapper mapper,
                                 @Qualifier("oracleJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(repository, mapper, d -> {
            return mapper.map(d, Invoice.class);
        });
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("INVOICE")
                .withProcedureName("INSERT_INVOICE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_invoice_code", Types.VARCHAR),
                        new SqlParameter("p_create_date", Types.TIMESTAMP),
                        new SqlParameter("p_total_amount", Types.FLOAT),
                        new SqlParameter("p_client_id", Types.VARCHAR),
                        new SqlParameter("p_insert_invoice", Types.NUMERIC),
                        new SqlParameter("p_products_code", Types.ARRAY, "INVOICE.VARCHAR2_LIST"),
                        new SqlParameter("p_products_name", Types.ARRAY, "INVOICE.VARCHAR2_LIST"),
                        new SqlParameter("p_products_quantity", Types.ARRAY, "INVOICE.NUMBER_LIST"),
                        new SqlParameter("p_products_unit_price", Types.ARRAY, "INVOICE.FLOAT_LIST"),
                        new SqlOutParameter("out_invoice_id", Types.VARCHAR)
                );
    }

    @Override
    public PageResult<Invoice> getAllByClientId(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDao> invoicesFound = repository.findAllByClientId(id, pageable);
        List<Invoice> invoices = getInvoices(invoicesFound);
        return Util.structureResponse(invoices, invoicesFound);
    }

    private List<Invoice> getInvoices(Page<InvoiceDao> invoicesFound) {
        return invoicesFound.getContent().stream()
                .map(Util::toModel)
                .toList();
    }

    @Override
    public PageResult<Invoice> getAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<InvoiceDao> invoicesFound = repository.findAll(pageable);
            List<Invoice> invoices = getInvoices(invoicesFound);
            return Util.structureResponse(invoices, invoicesFound);

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving invoices: " + e.getMessage(), e);
        }
    }



    @Override
    public Invoice registerInvoice(Invoice invoice, Client client) {

        String invoiceCode = UUID.randomUUID().toString();
        invoice.setCode(invoiceCode);
        invoice.setTotalAmount(100.0);
        LocalDateTime now = LocalDateTime.now();
        invoice.setCreatedDate(now);
        try {

            ProductInfo p = Util.structProduct(invoice.getProducts());
            Map<String, Object> params = Util.structureParams(invoice, client, p);
            Map<String, Object> out = simpleJdbcCall.execute(params);
            String insertedInvoiceId = (String) out.get("out_invoice_id");
            invoice.setCode(insertedInvoiceId);
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar la factura: " + e.getMessage(), e);
        }

        return invoice;
    }

}