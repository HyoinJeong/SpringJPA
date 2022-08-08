package com.kdt.lecture;

import com.kdt.lecture.repository.CustomerRepository;
import com.kdt.lecture.repository.domain.CustomerEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class JPATest {

    @Autowired
    CustomerRepository repository;

    @BeforeEach
    void setUp(){}

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Test
    void INSERT_TEST(){
        // Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("honggu");
        customer.setLastName("kang");
        customer.setAge(33);

        // When
        repository.save(customer);

        // Then
        CustomerEntity entity = repository.findById(1L).get();
        log.info("{} {}", entity.getFirstName(), entity.getLastName());
        log.info("age : {}", entity.getAge());
    }

    @Test
    @Transactional
    void DELETE_TEST(){
        // Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("honggu");
        customer.setLastName("kang");
        repository.save(customer);

        // When
        CustomerEntity entity = repository.findById(1L).get();
        entity.setFirstName("guppy");
        entity.setLastName("hong");


        // Then
        CustomerEntity updated = repository.findById(1L).get();
        log.info("{} {}", updated.getFirstName(), updated.getLastName());
    }
}
