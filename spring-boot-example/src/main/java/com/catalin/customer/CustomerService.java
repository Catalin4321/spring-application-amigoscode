package com.catalin.customer;

import com.catalin.exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getACustomer(Integer id){
        return customerDao.selectCustomersById(id).orElseThrow(()-> new ResourceNotFound("customer with id [%s] not found".formatted(id)));
    }
}
