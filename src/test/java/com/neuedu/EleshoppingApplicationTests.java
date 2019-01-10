package com.neuedu;

import com.neuedu.dao.CategoryMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.neuedu.dao.CategoryMapper")
public class EleshoppingApplicationTests {

    @Test
    public void contextLoads() {
    }

}

