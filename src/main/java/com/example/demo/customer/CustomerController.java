package com.example.demo.customer;


import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ApplicationContext context;

    @GetMapping()
    public List<Customer> getCustomers() {
        return customerService.retrieveCustomer();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.retrieveCustomer(id);
        if(!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping(params = "name")
    public List<Customer> getCustomer(@RequestParam(value = "name") String name) {
        return customerService.retrieveCustomer(name);
    }

    @PostMapping()
    public ResponseEntity<?> postCustomer(@Valid @RequestBody Customer body) {
        Customer customer = customerService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCustomer(@PathVariable Long id, @Valid @RequestBody Customer body) {
        Optional<Customer> customer = customerService.updateCustomer(id, body);
        if(!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        if(!customerService.deleteCustomer(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> postImage(@Valid @RequestBody Picture pic) {
        try
        {
            //This will decode the String which is encoded by using Base64 class
            byte[] imageByte = Base64.getDecoder().decode(pic.getImage());

            String directory= "upload/sample.jpg";

            new FileOutputStream(directory).write(imageByte);
            return ResponseEntity.ok().build();
        }
        catch(Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/photo/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String image)  throws IOException  {
        InputStream in = new FileInputStream(new File("upload/"+image));
        return IOUtils.toByteArray(in);
    }
}
