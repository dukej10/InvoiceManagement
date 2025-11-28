package co.com.credit.model.credit;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Credit {
    private String id;
    private String idClient;
    private BigDecimal initialCreditLimit;
    private BigDecimal currentCreditLimit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
