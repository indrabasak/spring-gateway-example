package com.basaki.k8s.controller;

import com.basaki.k8s.error.ErrorInfo;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

/**
 * {@code CustomErrorControllerTest} represents unit test for {@code
 * CustomErrorController}.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
public class CustomErrorControllerTest {

    @Mock
    private ErrorAttributes errorAttributes;

    @InjectMocks
    private CustomErrorController controller;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(request));

        response = new MockHttpServletResponse();
    }

    @Test
    public void testError() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", "test-message");
        attributes.put("error", "test-error");

        when(errorAttributes.getErrorAttributes(any(WebRequest.class),
                anyBoolean())).thenReturn(attributes);

        ResponseEntity<ErrorInfo>
                entity = controller.error(request, response);
        assertNotNull(entity);
        ErrorInfo errorInfo = entity.getBody();
        assertNotNull(errorInfo);
        assertEquals("test-message", errorInfo.getMessage());
        assertNotNull(controller.getErrorPath());
    }

    @Test
    public void testErrorWithException() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", "test-message");
        attributes.put("error", "test-error");

        when(errorAttributes.getErrorAttributes(any(WebRequest.class),
                anyBoolean())).thenReturn(attributes);

        RequestRejectedException exp =
                new RequestRejectedException("Just a test.");
        when(errorAttributes.getError(any(WebRequest.class))).thenReturn(exp);

        ResponseEntity<ErrorInfo>
                entity = controller.error(request, response);
        assertNotNull(entity);
        ErrorInfo errorInfo = entity.getBody();
        assertNotNull(errorInfo);
        assertEquals("test-message", errorInfo.getMessage());
        assertNotNull(controller.getErrorPath());
        assertEquals(400, errorInfo.getCode());
    }
}
