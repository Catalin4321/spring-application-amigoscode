package com.catalin.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    //    @RequestMapping(path = "api/v1/customer", method = RequestMethod.GET)
//    public List<Customer> getCustomers(){
//        return customers;
//    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") Integer id){
        return customerService.getACustomer(id);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id){
        customerService.deleteCustomerById(id);
    }

    @PutMapping("{id}")
    public void changeCustomer(@PathVariable("id") Integer id, @RequestBody CustomerUpdateRequest updateRequest){
        customerService.changeCustomerById(id, updateRequest);
    }
}

