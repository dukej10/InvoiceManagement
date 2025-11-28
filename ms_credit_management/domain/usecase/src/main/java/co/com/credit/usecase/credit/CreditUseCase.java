package co.com.credit.usecase.credit;

import co.com.credit.model.credit.gateways.CreditRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class CreditUseCase {

    private final CreditRepository creditRepository;
}
