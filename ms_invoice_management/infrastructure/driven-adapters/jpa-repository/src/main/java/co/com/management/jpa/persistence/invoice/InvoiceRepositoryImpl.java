package co.com.management.jpa.persistence.invoice;

import co.com.management.jpa.config.oracle.OracleProcedureProperties;
import co.com.management.jpa.helper.AdapterOperations;
import co.com.management.jpa.helper.Util;
import co.com.management.model.PageResult;
import co.com.management.model.ProductInfo;
import co.com.management.model.client.Client;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.GeneralException;
import co.com.management.model.exception.NoDataFoundException;
import co.com.management.model.invoice.Invoice;
import co.com.management.model.invoice.gateways.InvoiceRepository;
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

@Repository
public class InvoiceRepositoryImpl extends AdapterOperations<Invoice, InvoiceDao, String, InvoiceDaoRepository>
        implements InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;
    private  final SimpleJdbcCall simpleJdbcCall;
    private final OracleProcedureProperties props;
    public InvoiceRepositoryImpl(InvoiceDaoRepository repository, ObjectMapper mapper,
                                 @Qualifier("oracleJdbcTemplate") JdbcTemplate jdbcTemplate,
                                 OracleProcedureProperties props) {
        super(repository, mapper, d -> {
            return mapper.map(d, Invoice.class);
        });
        this.jdbcTemplate = jdbcTemplate;
        this.props = props;
        this.simpleJdbcCall = initSimpleJdbCall(jdbcTemplate);
    }

    private SimpleJdbcCall initSimpleJdbCall(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(props.getSchema())
                .withProcedureName(props.getName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_invoice_code", Types.VARCHAR),
                        new SqlParameter("p_create_date", Types.TIMESTAMP),
                        new SqlParameter("p_total_amount", Types.NUMERIC),
                        new SqlParameter("p_client_id", Types.VARCHAR),
                        new SqlParameter("p_insert_invoice", Types.NUMERIC),
                        new SqlParameter("p_products_code", Types.ARRAY,
                                props.getArrayTypes().getVarchar2List()),
                        new SqlParameter("p_products_name", Types.ARRAY,
                                props.getArrayTypes().getVarchar2List()),
                        new SqlParameter("p_products_quantity", Types.ARRAY,
                                props.getArrayTypes().getNumberList()),
                        new SqlParameter("p_products_unit_price", Types.ARRAY,
                                props.getArrayTypes().getFloatList()),

                        new SqlOutParameter("out_invoice_id", Types.VARCHAR)
                );

    }

    @Override
    public PageResult<Invoice> getAllByClientId(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDao> invoicesFound = repository.findAllPageableByClientId(id, pageable);
        if(invoicesFound.isEmpty()) throw new DataFoundException("No hay información");
        List<Invoice> invoices = getInvoices(invoicesFound);
        return Util.structureResponse(invoices, invoicesFound);
    }

    private List<Invoice> getInvoices(Page<InvoiceDao> invoicesFound) {
        return invoicesFound.getContent().stream()
                .map(Util::toModel)
                .toList();
    }

    @Override
    public PageResult<Invoice> getAllPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceDao> invoicesFound = repository.findAll(pageable);
        if(invoicesFound.isEmpty()) throw new DataFoundException("No hay información");
        List<Invoice> invoices = getInvoices(invoicesFound);
        return Util.structureResponse(invoices, invoicesFound);
    }
    @Override
    public Invoice registerInvoice(Invoice invoice, Client client) {

        invoice.setClientId(client.getId());
        LocalDateTime now = LocalDateTime.now();
        invoice.setCreatedDate(now);
        try {

            ProductInfo p = Util.structProduct(invoice.getProducts());
            Map<String, Object> params = Util.structureParams(invoice, client, p, props.getArrayTypes());
            Map<String, Object> out = simpleJdbcCall.execute(params);
            String insertedInvoiceId = (String) out.get("out_invoice_id");
            invoice.setId(insertedInvoiceId);
        } catch (Exception e) {
           throw new GeneralException("Error al insertar la factura");
        }

        return invoice;
    }

    @Override
    public Invoice getById(String id) {
        return repository.findById(id)
                .map(this::toEntity)
                .orElseThrow(NoDataFoundException::new);
    }

    @Override
    public void deleteInvoice(String id) {
        repository.deleteById(id);
    }

    private List<Invoice> findAllByClientId(String id) {
        return repository.findAllByClientId(id).stream().map(
             this::toEntity
        ).toList();
    }

    @Override
    public void deleteAllByClientId(String clientId) {
        List<Invoice> invoices  = findAllByClientId(clientId);
        List<String> invoicesId = invoices.stream().map(Invoice::getId).toList();
        repository.deleteAllById(invoicesId);
    }


}