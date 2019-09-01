package com.basaki.k8s.error;

import com.basaki.k8s.error.exception.DataNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;

/**
 * {@code ExceptionProcessorTest} represents unit test for {@code
 * ExceptionProcessor}.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
public class ExceptionProcessorTest {

    private ExceptionProcessor processor;
    private MockHttpServletRequest request;
    private String message;
    private String path;

    @Before
    public void setUp() throws Exception {
        processor = new ExceptionProcessor();
        request = new MockHttpServletRequest();
        message = "some message";
        path = "/some/path";
        request.setRequestURI(path);
        request.setPathInfo(path);
    }

    @Test
    public void testHandleDataNotFoundException() throws Exception {
        DataNotFoundException exception = new DataNotFoundException(message);
        ErrorInfo info =
                processor.handleDataNotFoundException(request, exception);
        validate(info, HttpStatus.NOT_FOUND, message);
    }

    private void validate(ErrorInfo info, HttpStatus status, String msg) {
        assertEquals(msg, info.getMessage());
        assertEquals(status.value(), info.getCode());
        assertEquals(status.getReasonPhrase(), info.getType());
        assertEquals(path, info.getPath());
    }
}
