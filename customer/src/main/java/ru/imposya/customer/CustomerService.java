package ru.imposya.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.imposya.clients.fraud.FraudCheckResponse;
import ru.imposya.clients.fraud.FraudClient;
import ru.imposya.clients.notification.NotificationClient;
import ru.imposya.clients.notification.NotificationRequest;

@Service
@AllArgsConstructor
public class CustomerService {

    private final RestTemplate restTemplate;
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse response = fraudClient.isFraudster(customer.getId());

        if (response != null && response.isFraudster()) {
            throw new IllegalStateException("fraudster!");
        }

        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to my petproject!", customer.getFirstName())
                )
        );
    }
}
