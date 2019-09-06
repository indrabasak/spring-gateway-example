package com.basaki.edge.security.basic;

import com.basaki.edge.exception.AuthenticationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnitParamsRunner.class)
public class BasicAuthProviderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Parameters
    public void testAuthenticate(BasicAuthProvider provider,
                                 String user, String password,
                                 Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        assertNotNull(provider);
        assertNotNull(provider.getUser());
        assertNotNull(provider.getPassword());

        provider.authenticate(user, password);
    }

    public Iterable<Object[]> parametersForTestAuthenticate() {
        return Arrays.asList(new Object[][]{
                {new BasicAuthProvider("user01", "pwd01"),
                        "user02", "pwd01", AuthenticationException.class},
                {new BasicAuthProvider("user01", "pwd01"),
                        "user01", "pwd02", AuthenticationException.class},
                {new BasicAuthProvider("user01", "pwd01"),
                        "user01", "pwd01", null},
        });
    }
}
