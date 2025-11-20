package co.com.management.jpa.persistence.invoice;

import co.com.management.jpa.persistence.ProductDao;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;

    @Column(length = 11, nullable = false)
    private LocalDate expDate;

    @Column(length = 11, nullable = false)
    private String status;

    @Column(length = 11, nullable = false)
    private Double totalAmount;

    @OneToMany(mappedBy = "invoice")
    List<ProductDao> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    ClientDao client;

    @Column(length = 11, nullable = false)
    private LocalDateTime createdDate;

    @Column(length = 11, nullable = false)
    private LocalDateTime updatedDate;


    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
