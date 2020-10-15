//: guru.sfg.beer.order.service.services.CustomerService.java


package guru.sfg.beer.order.service.services;


import com.google.common.collect.ImmutableList;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.web.mappers.CustomerMapper;
import guru.sfg.beer.order.service.web.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDto> getAllCustomers() {
        return this.customerRepository.findAll().stream()
                .map(customer -> this.customerMapper.customerToDto(customer))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public Optional<CustomerDto> findCustomerById(UUID id) {
        return this.customerRepository.findById(id)
                .map(this.customerMapper::customerToDto);
    }

}///:~