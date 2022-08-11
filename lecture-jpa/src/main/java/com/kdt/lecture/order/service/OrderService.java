package com.kdt.lecture.order.service;

import com.kdt.lecture.domain.order.Order;
import com.kdt.lecture.domain.order.OrderRepository;
import com.kdt.lecture.order.converter.OrderConverter;
import com.kdt.lecture.order.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderConverter orderConverter;

    @Transactional // transaction.begin()과 transcation.commit()을 안해도 됨
    public String save(OrderDto dto){
        // 1. dto -> entity 변환 (준영속)
        Order order = orderConverter.convertOrder(dto);
        // 2. orderRepository.save(entity) -> 영속화
        Order entity = orderRepository.save(order);
        // 3. 결과 반환
        return entity.getUuid();

    }

    public void findAll(){

    }

    public void findOne(){

    }
}
