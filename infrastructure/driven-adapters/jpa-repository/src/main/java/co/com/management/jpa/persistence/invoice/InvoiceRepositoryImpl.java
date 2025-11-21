package co.com.management.jpa.persistence.invoice;

import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.jpa.helper.OracleHelper;
import co.com.management.model.PageResult;
import co.com.management.model.client.Client;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
import co.com.management.model.product.Product;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

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
                        new SqlParameter("P_products_code", Types.ARRAY, "INVOICE.VARCHAR2_LIST"),
                        new SqlParameter("p_products_name", Types.ARRAY, "INVOICE.VARCHAR2_LIST"),
                        new SqlParameter("p_products_quantity", Types.ARRAY, "INVOICE.NUMBER_LIST"),
                        new SqlParameter("p_products_unit_price", Types.ARRAY, "INVOICE.FLOAT_LIST"),
                        new SqlOutParameter("out_invoice_id", Types.VARCHAR)
                );
    }

    @Override
    public List<Invoice> getAll() {
        return List.of();
    }

    @Override
    public PageResult<Invoice> getAllByClientId(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDao> invoicesFound = repository.findAllByClientId(id, pageable);
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
        );
    }

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
        );
    }

    @Override
    public Invoice registerInvoice(Invoice invoice, Client client) {

        int userActive = client.getState() ? 1 : 0;
        String invoiceCode = UUID.randomUUID().toString();
        invoice.setCode(invoiceCode);
        invoice.setTotalAmount(100.0);
        BigDecimal amountT  = BigDecimal.valueOf(invoice.getTotalAmount());
        LocalDateTime now = LocalDateTime.now();
        invoice.setCreatedDate(now);
        Timestamp timeNow =  Timestamp.valueOf(now);

        try {

            String[] names = invoice.getProducts().stream()
                    .map(Product::getName)
                    .toArray(String[]::new);

            Integer[] quantities = invoice.getProducts().stream()
                    .map(Product::getQuantity)
                    .toArray(Integer[]::new);

            Float[] prices = invoice.getProducts().stream()
                    .map(Product::getUnitPrice)
                    .toArray(Float[]::new);

            String[] productCodes = invoice.getProducts().stream()
                    .map(p -> UUID.randomUUID().toString())
                    .toArray(String[]::new);

            IntStream.range(0, invoice.getProducts().size())
                    .forEach(i -> invoice.getProducts().get(i).setCode(productCodes[i]));

            Map<String, Object> params = Map.of(
                    "p_invoice_code",         invoice.getCode(),
                    "p_create_date",          timeNow,
                    "p_total_amount",        amountT,
                    "p_client_id",            client.getId(),
                    "p_insert_invoice",       userActive,
                    "P_products_code",      new OracleHelper("INVOICE.VARCHAR2_LIST", productCodes),
                    "p_products_name", new OracleHelper("INVOICE.VARCHAR2_LIST", names),
                    "p_products_quantity",    new OracleHelper("INVOICE.NUMBER_LIST", quantities),
                    "p_products_unit_price",  new OracleHelper("INVOICE.FLOAT_LIST", prices)
            );


            Map<String, Object> out = simpleJdbcCall.execute(params);
            String insertedInvoiceId = (String) out.get("out_invoice_id");
            invoice.setCode(invoice.getCode());
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar la factura: " + e.getMessage(), e);
        }

        return invoice;
    }
}