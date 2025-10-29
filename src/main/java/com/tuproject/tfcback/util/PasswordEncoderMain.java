package com.tuproject.tfcback.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderMain {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword1 = "admin";
        String encodedPassword1 = encoder.encode(rawPassword1);
        System.out.println("Admin hash: " + encodedPassword1);

        String rawPassword2 = "user";
        String encodedPassword2 = encoder.encode(rawPassword2);
        System.out.println("User hash: " + encodedPassword2);
    }
}
