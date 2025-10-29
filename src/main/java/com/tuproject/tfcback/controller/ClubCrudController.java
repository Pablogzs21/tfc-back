package com.tuproject.tfcback.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuproject.tfcback.dto.ClubDTO;
import com.tuproject.tfcback.model.Club;
import com.tuproject.tfcback.repository.ClubRepository;

@RestController
@RequestMapping("/api/admin/clubs")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@PreAuthorize("hasRole('ADMIN')")
public class ClubCrudController {

    private final ClubRepository clubRepository;

    public ClubCrudController(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    // 🔹 Listar todos los clubs (DTOs simples)
    @GetMapping
    public List<ClubDTO> listar() {
        return clubRepository.findAllDto();
    }

    // 🔹 Obtener un club por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getById(@PathVariable Long id) {
        return clubRepository.findById(id)
                .map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Crear un nuevo club
    @PostMapping
    public ResponseEntity<ClubDTO> crear(@RequestBody Club body) {
        if (body.getNombre() == null || body.getNombre().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Club saved = clubRepository.save(body);
        return ResponseEntity.ok(toDTO(saved));
    }

    // 🔹 Actualizar un club existente
    @PutMapping("/{id}")
    public ResponseEntity<ClubDTO> actualizar(@PathVariable Long id, @RequestBody Club body) {
        return clubRepository.findById(id)
                .map(c -> {
                    if (body.getNombre() != null)    c.setNombre(body.getNombre());
                    if (body.getDireccion() != null) c.setDireccion(body.getDireccion());
                    if (body.getTelefono() != null)  c.setTelefono(body.getTelefono());
                    if (body.getLat() != null)       c.setLat(body.getLat());
                    if (body.getLng() != null)       c.setLng(body.getLng());
                    Club saved = clubRepository.save(c);
                    return ResponseEntity.ok(toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Eliminar un club
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!clubRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clubRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ClubDTO toDTO(Club c) {
        ClubDTO d = new ClubDTO();
        d.setId(c.getId());
        d.setNombre(c.getNombre());
        d.setDireccion(c.getDireccion());
        d.setTelefono(c.getTelefono());
        d.setLat(c.getLat());
        d.setLng(c.getLng());
        return d;
    }
}
