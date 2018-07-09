package com.curchat;

import com.curchat.config.CurchatApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CurchatApplication.class})
@AutoConfigureTestDatabase
public class CurchatApplicationTests {

	@Test
	public void contextLoads() {
	}

}
