package com.tuproject.tfcback.controller;

import com.tuproject.tfcback.model.Usuario;
import com.tuproject.tfcback.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL USERS (solo para admin)
    @GetMapping
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    // GET USER BY ID (solo para admin)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        Optional<Usuario> userOpt = usuarioRepository.findById(id);
        return userOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE USER (solo para admin)
    @PostMapping
    public Usuario createUser(@RequestBody Usuario user) {
        // Encriptar password antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usuarioRepository.save(user);
    }

    // UPDATE USER (solo para admin)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable Long id, @RequestBody Usuario updatedUser) {
        Optional<Usuario> userOpt = usuarioRepository.findById(id);
        if (userOpt.isPresent()) {
            Usuario existingUser = userOpt.get();
            existingUser.setNombre(updatedUser.getNombre());
            existingUser.setApellidos(updatedUser.getApellidos());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setFechaNacimiento(updatedUser.getFechaNacimiento());

            // Si viene una password nueva, encriptarla
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            usuarioRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE USER (solo para admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET CURRENT USER (para el usuario autenticado: USER o ADMIN)
    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser(Authentication authentication) {
        String username = authentication.getName(); // nombre de usuario autenticado
        Optional<Usuario> userOpt = usuarioRepository.findByUsername(username);

        return userOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 // GET USER DATA
    @GetMapping("/user/data")
    public Usuario getUserData(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    // GET ADMIN DATA
    @GetMapping("/admin/data")
    public Usuario getAdminData(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username).orElse(null);
    }

}
