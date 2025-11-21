package co.com.management.jpa.persistence.invoice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="products")
public class ProductDao {

    @Id
    private String code;

    @Column(name = "name_product", length = 11, nullable = false)
    private String name;

    @Column(length = 11, nullable = false)
    private Integer quantity;

    @Column(name = "unit_price",length = 100, nullable = false)
    private Float unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="invoice_id")
    private InvoiceDao invoice;
}
