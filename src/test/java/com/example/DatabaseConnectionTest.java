package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetUserById() {
        // 데이터베이스에서 ID가 1인 사용자 조회
        User user = userMapper.getUserByIdTest(1);

        if(Objects.isNull(user)){
            System.out.println("null");
        }
    }
}
