package com.tuproject.tfcback.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuproject.tfcback.dto.LoginRequest;
import com.tuproject.tfcback.model.Usuario;
import com.tuproject.tfcback.repository.UsuarioRepository;
import com.tuproject.tfcback.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepo;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UsuarioRepository usuarioRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Intentando login con username: " + loginRequest.getUsername() +
                           ", password: " + loginRequest.getPassword());
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            Usuario user = usuarioRepo.findByUsername(loginRequest.getUsername()).get();

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
