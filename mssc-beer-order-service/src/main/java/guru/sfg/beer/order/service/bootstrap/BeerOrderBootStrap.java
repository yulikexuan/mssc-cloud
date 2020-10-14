package guru.sfg.beer.order.service.bootstrap;

import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.UUID;


/**
 * Created by jt on 2019-06-06.
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class BeerOrderBootStrap implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final String BEER_4_UPC = "0072890000224";
    public static final String BEER_5_UPC = "0071990316006";
    public static final String BEER_6_UPC = "0640265808593";
    public static final String BEER_7_UPC = "0640265808609";

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCustomerData();
    }

    private void loadCustomerData() {

        if (this.customerRepository.findAllByCustomerNameLike(
                BeerOrderBootStrap.TASTING_ROOM).size() == 0) {

            Customer customer = customerRepository.save(Customer.builder()
                    .customerName(TASTING_ROOM)
                    .apiKey(UUID.randomUUID())
                    .build());

            log.info(">>>>>>> Saved one Tasting Room Customer wity id {}",
                    customer.getId());
        }
    }
}
