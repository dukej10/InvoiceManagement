package co.com.management.jpa.persistence.invoice;

import co.com.management.jpa.persistence.client.ClientDao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="invoices")
public class InvoiceDao {

    @Id
    @Column(length = 36)
    private String code;

    @Column(name="total_amount",nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
    List<ProductDao> products;

    @Column(name="client_id", length = 36)
    String clientId;

    @Column(name= "created_at")
    private LocalDateTime createdDate;

}
