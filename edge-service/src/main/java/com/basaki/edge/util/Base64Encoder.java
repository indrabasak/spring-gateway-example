package com.basaki.edge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Service
@Slf4j
public class Base64Encoder {

    private static final String CHARSET_NAME = "UTF-8";

    public String encode(String plainString) {
        try {
            return Base64.getEncoder().encodeToString(plainString.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            log.warn("UTF-8 encoding not supported");
            return Base64.getEncoder().encodeToString(plainString.getBytes());
        }
    }

    public String decode(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
