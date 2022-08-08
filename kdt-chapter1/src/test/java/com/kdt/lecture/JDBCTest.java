package com.kdt.lecture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@SpringBootTest
public class JDBCTest {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASS = "";

    static final String DROP_TABLE_SQL = "DROP TABLE customers IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";
    static final String INSERT_SQL = "INSERT INTO customers (id, first_name, last_name) VALUES(1, 'honggu', 'kang')";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void jdbc_sample() {
        try {
            Class.forName(JDBC_DRIVER);

            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            log.info("Connection 획득");
            // CONNECTION

            Statement statement = connection.createStatement();
            // STATEMENT
            log.info("Statement 획득");
            log.info("쿼리 실행");
            statement.executeUpdate(DROP_TABLE_SQL);
            statement.executeUpdate(CREATE_TABLE_SQL);
            log.info("CREATED TABLE");

            statement.executeUpdate(INSERT_SQL);
            log.info("INSERTED CUSTOMER INFORMATION");
            ResultSet resultSet = statement.executeQuery("SELECT id, first_name, last_name FROM customers WHERE id = 1");
            // RESULT_SET

            while(resultSet.next()) {
                log.info(resultSet.getString("first_name"));
            }

            log.info("반납, 반납");
            statement.close();
            connection.close();
            // CLOSE
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void jdbcTemplate_sample(){
        jdbcTemplate.update(DROP_TABLE_SQL);
        jdbcTemplate.update(CREATE_TABLE_SQL);
        log.info("CREATED TABLE USING JDBC TEMPLATE");

        jdbcTemplate.update(INSERT_SQL);
        log.info("INSERTED CUSTOMER INFORMATION USING JDBC TEMPLATE");

        String fullName = jdbcTemplate.queryForObject(
                "select * from customers where id=1",
                (resultSet,i)->resultSet.getString("first_name")+" "+resultSet.getString("last_name")
        );
        log.info("FULL_NAME : {}", fullName);
    }
}
