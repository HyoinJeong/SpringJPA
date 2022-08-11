package com.kdt.lecture.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.kdt.lecture.domain.order.OrderStatus.OPENED;

@Slf4j
@SpringBootTest
public class ProxyTest {

    @Autowired
    private EntityManagerFactory emf;

    private String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // 주문 엔티티
        Order order = new Order();
        order.setUuid(uuid);
        order.setMemo("부재시 전화주세요.");
        order.setOrderDateTime(LocalDateTime.now());
        order.setOrderStatus(OPENED);

        entityManager.persist(order);

        // 회원 엔티티
        Member member = new Member();
        member.setName("kanghonggu");
        member.setNickName("guppy.kang");
        member.setAge(33);
        member.setAddress("서울시 동작구만 움직이면쏜다.");
        member.setDescription("KDT 화이팅");

        member.addOrder(order); // 연관관계 편의 메소드 사용
        entityManager.persist(member);
        transaction.commit();
    }


    @Test
    void proxy() {
        EntityManager entityManager = emf.createEntityManager();

        Order order = entityManager.find(Order.class,uuid);

        Member member = order.getMember();
        log.info("MEMBER USE BEFORE IS-LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member)); // 아직 사용하기 전이므로 프록시객체 -> false가 나와야함
        String nickName = member.getNickName();// 이런게 사용하는 것
        log.info("MEMBER USE BEFORE IS-LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member)); // 프록시객체 사용후이므로 entity객체 -> true 나와야함
    }

    @Test
    void move_persist() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Order order = entityManager.find(Order.class,uuid);// 영속상태

        transaction.begin();

        OrderItem item = new OrderItem(); // 준영속상태
        item.setQuantity(10);
        item.setPrice(1000);

        order.addOrderItem(item); // 영속상태로 바뀜

        transaction.commit(); // flush

        // 고아 상태
        entityManager.clear();
        Order order2 = entityManager.find(Order.class,uuid);

        transaction.begin();

        order2.getOrderItems().remove(0);
        
        transaction.commit();

    }
}
