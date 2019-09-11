package com.basaki.edge;

import com.basaki.edge.security.SecurityAuthProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EdgeApplicationTest {

    @Autowired
    private SecurityAuthProperties properties;

    @Test
    public void testExceptionHandlerExceptionResolver() {
        assertNotNull(properties);
        assertNotNull(properties.getRoutes());
        assertTrue(properties.getRoutes().size() > 0);
    }
}