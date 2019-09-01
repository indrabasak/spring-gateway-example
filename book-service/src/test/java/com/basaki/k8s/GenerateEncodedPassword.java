package com.basaki.k8s;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@code GenerateEncodedPassword} is a utility class for creating encoded
 * passwords.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
public class GenerateEncodedPassword {

    public static void main(String... args) {
        PasswordEncoder passwordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String encodedPwd = passwordEncoder.encode("passwordA");
        System.out.println("passwordA: " + encodedPwd);

        encodedPwd = passwordEncoder.encode("passwordB");
        System.out.println("passwordB: " + encodedPwd);

    }
}
