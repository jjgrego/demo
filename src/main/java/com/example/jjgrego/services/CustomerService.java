package com.example.jjgrego.services;

import com.example.jjgrego.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID id);
    List<CustomerDTO> getCustomers();

    CustomerDTO addCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customer);

    boolean deleteCustomerById(UUID id);

    void patchCustomerById(UUID id, CustomerDTO customer);

}
