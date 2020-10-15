//: guru.sfg.beer.order.service.services.ICustomerService.java


package guru.sfg.beer.order.service.services;


import guru.sfg.beer.order.service.web.model.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ICustomerService {

    List<CustomerDto> getAllCustomers();
    Optional<CustomerDto> findCustomerById(UUID id);

}///:~