package ru.imposya.fraud;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.imposya.clients.fraud.FraudCheckResponse;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudController {

    private final FraudCheckService fraudCheckService;

    @GetMapping(path = "/{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId")Integer customerId) {
        boolean isFradulentCustomer = fraudCheckService.isFraudulent(customerId);
        log.info("Fraud check request for customer {}", customerId);
        return new FraudCheckResponse(isFradulentCustomer);
    }
}
