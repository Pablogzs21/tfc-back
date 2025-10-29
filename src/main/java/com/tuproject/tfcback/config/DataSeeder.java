package com.tuproject.tfcback.config;

import com.tuproject.tfcback.model.Usuario;
import com.tuproject.tfcback.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initUsers(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin")); // contraseña cifrada
                admin.setRole("ROLE_ADMIN");
                admin.setNombre("Administrador");
                admin.setApellidos("del sistema");
                repo.save(admin);
                System.out.println("✅ Usuario admin creado");
            }

            if (!repo.existsByUsername("user")) {
                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(encoder.encode("user"));
                user.setRole("ROLE_USER");
                user.setNombre("Usuario");
                user.setApellidos("normal");
                repo.save(user);
                System.out.println("✅ Usuario user creado");
            }
        };
    }
}
