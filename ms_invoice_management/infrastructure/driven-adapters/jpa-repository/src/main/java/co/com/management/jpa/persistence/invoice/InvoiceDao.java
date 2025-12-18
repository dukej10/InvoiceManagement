package co.com.management.jpa.persistence.invoice;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="invoices")
public class InvoiceDao {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name="total_amount",nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<ProductDao> products;

    @Column(name="client_id", length = 36)
    String clientId;

    @Column(name= "created_at")
    private LocalDateTime createdDate;

}
