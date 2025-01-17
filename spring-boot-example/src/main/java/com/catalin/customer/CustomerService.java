package com.catalin.customer;

import com.catalin.exception.DuplicateResourceException;
import com.catalin.exception.RequestValidationException;
import com.catalin.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getACustomer(Integer id){
        return customerDao.selectCustomersById(id).orElseThrow(()-> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        // add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer id){
        if(!customerDao.existsPersonWithId(id)){
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }

        customerDao.deleteCustomerById(id);
    }

    public void changeCustomerById(Integer id, CustomerUpdateRequest updateRequest){
        Customer customer = getACustomer(id);

        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes = true;
        }

        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            if(customerDao.existsPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException( "email already taken" );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no changes found");
        }

        customerDao.insertCustomer(customer);
    }
}
