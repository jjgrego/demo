package com.example.jjgrego.mappers;

import com.example.jjgrego.entities.Customer;
import com.example.jjgrego.model.CustomerDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO  customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
