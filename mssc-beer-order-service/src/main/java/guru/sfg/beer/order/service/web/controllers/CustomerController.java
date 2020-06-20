//: guru.sfg.beer.order.service.web.controllers.CustomerController.java


package guru.sfg.beer.order.service.web.controllers;


import com.google.common.collect.ImmutableList;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.web.mappers.CustomerMapper;
import guru.sfg.beer.order.service.web.model.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerRepository customerRepository,
                              CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @GetMapping()
    public List<CustomerDto> getAllCustomers() {

        return this.customerRepository.findAll().stream()
                .map(customer -> this.customerMapper.customerToDto(customer))
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable("id") UUID uuid) {

        Customer customer = this.customerRepository.findById(uuid).orElse(null);

        if (Objects.nonNull(customer)) {
            return this.customerMapper.customerToDto(customer);
        } else {
            return null;
        }
    }

}///:~