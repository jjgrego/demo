package com.example.jjgrego.controllers;

import com.example.jjgrego.entities.Customer;
import com.example.jjgrego.errors.NotFoundException;
import com.example.jjgrego.mappers.CustomerMapper;
import com.example.jjgrego.model.CustomerDTO;
import com.example.jjgrego.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testListCustomers() {
        List<CustomerDTO> customers = customerController.getCustomers();
        assertThat(customers.size()).isEqualTo(4);
    }


    @Test
    @Rollback
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> customers = customerController.getCustomers();
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void testCustNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getById(UUID.randomUUID());
        });
    }


    @Test
    void testGetCustomerById() {
        Customer cust = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getById(cust.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    void saveNewCustomer() {
        CustomerDTO newCust = CustomerDTO.builder()
                .name("wild thing")
                .build();
        ResponseEntity re = customerController.addNewCustomer(newCust);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));

        assertThat(re.getHeaders().getLocation()).isNotNull();
        String[] locHeaders = re.getHeaders().getLocation().getPath().split("/");
        UUID id = UUID.fromString(locHeaders[4]);

        Customer aCust = customerRepository.findById(id).get();
        assertThat(aCust).isNotNull();

    }

    @Test
    void updateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO cDTO = customerMapper.customerToCustomerDTO(customer);

        cDTO.setId(null);
        cDTO.setVersion(null);
        final String custName = "UPDATED";

        cDTO.setName(custName);
        ResponseEntity re = customerController.updateCustomerById(customer.getId(), cDTO);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updated = customerRepository.findById((customer.getId())).get();
        assertThat(updated.getName()).isEqualTo(custName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }


    @Test
    void testDeleteById() {
        Customer c = customerRepository.findAll().get(0);
        UUID id = c.getId();

        ResponseEntity re = customerController.deleteCustById(id);
        assertThat(re.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testDeleteByIdNotFound() {
        CustomerDTO cust = CustomerDTO.builder()
                .name("bob")
                .build();

        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), cust);
        });

    }


}
