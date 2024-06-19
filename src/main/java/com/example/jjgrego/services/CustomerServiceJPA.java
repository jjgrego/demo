package com.example.jjgrego.services;

import com.example.jjgrego.entities.Customer;
import com.example.jjgrego.mappers.CustomerMapper;
import com.example.jjgrego.model.CustomerDTO;
import com.example.jjgrego.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customer) {
        Customer c = customerRepository.save(customerMapper.customerDtoToCustomer(customer));
        log.info("SAVED customer:::  {}", c);
        CustomerDTO newCust = customerMapper.customerToCustomerDTO(c);
        log.info("returning saved CustomerDTO:  {}", newCust);
        return newCust;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO c) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setName(c.getName());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDTO(customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public boolean deleteCustomerById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void patchCustomerById(UUID id, CustomerDTO customer) {
    }
}
