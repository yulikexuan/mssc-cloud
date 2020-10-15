//: guru.sfg.beer.order.service.web.controllers.CustomerController.java


package guru.sfg.beer.order.service.web.controllers;


import guru.sfg.beer.order.service.services.ICustomerService;
import guru.sfg.beer.order.service.services.NotFoundException;
import guru.sfg.beer.order.service.web.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return this.customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable("id") UUID uuid) {
        return this.customerService.findCustomerById(uuid)
                .orElseThrow(() -> new NotFoundException(
                        String.format(">>>>>>> Customer not found. id: %s", uuid)));
    }

}///:~