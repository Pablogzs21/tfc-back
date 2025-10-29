package com.tuproject.tfcback.controller;

import com.tuproject.tfcback.model.Club;
import com.tuproject.tfcback.repository.ClubRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@PreAuthorize("hasRole('ADMIN')")
public class ClubController {

    private final ClubRepository clubRepository;

    public ClubController(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @GetMapping
    public List<Club> listar() {
        return clubRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getById(@PathVariable Long id) {
        return clubRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Club> crear(@RequestBody Club club) {
        if (club.getNombre() == null || club.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }
        Club nuevo = clubRepository.save(club);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> actualizar(@PathVariable Long id, @RequestBody Club club) {
        return clubRepository.findById(id)
                .map(c -> {
                    c.setNombre(club.getNombre());
                    c.setDireccion(club.getDireccion());
                    c.setTelefono(club.getTelefono());
                    return ResponseEntity.ok(clubRepository.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!clubRepository.existsById(id)) return ResponseEntity.notFound().build();
        clubRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
