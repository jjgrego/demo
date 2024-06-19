package com.example.jjgrego.controllers;

import com.example.jjgrego.errors.NotFoundException;
import com.example.jjgrego.model.CustomerDTO;
import com.example.jjgrego.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController

public class CustomerController {
    public static final String CUSTOMER = "/v1/api/customer";
    public static final String CUSTOMER_ID = CUSTOMER + "/{customerId}";

    private final CustomerService customerService;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotfoundException() {
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomerDTO> getCustomers(){
        return customerService.getCustomers();
    }

    @GetMapping(CUSTOMER_ID)
    public CustomerDTO getById(UUID id){
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER)
    public ResponseEntity<CustomerDTO> addNewCustomer(@Validated @RequestBody CustomerDTO customer){
        CustomerDTO saved =  customerService.addCustomer(customer);
        log.info("saved customer id:   {}", saved.getId().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER + "/" + saved.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO cust){
        if(customerService.updateCustomerById(id, cust).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping(CUSTOMER_ID)
    public ResponseEntity deleteCustById(@PathVariable("customerId") UUID id ){
         customerService.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO cust){
        customerService.patchCustomerById(id, cust);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private HttpHeaders createHttpHeaders(CustomerDTO customer){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER + "/" + customer.getId());
        return headers;
    }
}
