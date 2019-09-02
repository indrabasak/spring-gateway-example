package com.basaki.edge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class Base64Encoder {

    public String encode(String plainString) {
        return Base64.getEncoder().encodeToString(plainString.getBytes(StandardCharsets.UTF_8));
    }

    public String decode(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
