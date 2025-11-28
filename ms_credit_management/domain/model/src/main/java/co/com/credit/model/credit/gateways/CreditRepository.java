package co.com.credit.model.credit.gateways;

import co.com.credit.model.credit.Credit;

public interface CreditRepository {

    Credit openCredit(String idClient);

    Credit updateCredit(String idClient, Double amountInvoice);

    Credit closeCredit(String idClient);
}
