package com.tuproject.tfcback.controller;

import com.tuproject.tfcback.model.Club;
import com.tuproject.tfcback.model.Pista;
import com.tuproject.tfcback.repository.ClubRepository;
import com.tuproject.tfcback.repository.PistaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/pistas")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@PreAuthorize("hasRole('ADMIN')")
public class PistaCrudController {

    private final PistaRepository pistaRepository;
    private final ClubRepository clubRepository;

    public PistaCrudController(PistaRepository pistaRepository, ClubRepository clubRepository) {
        this.pistaRepository = pistaRepository;
        this.clubRepository = clubRepository;
    }

  
    public static class PistaAdminDTO {
        private Long id;
        private String nombre;
        private String tipo;
        private BigDecimal precioHora;
        private Long clubId;
        private String clubNombre;

        public PistaAdminDTO() {}
        public PistaAdminDTO(Long id, String nombre, String tipo, BigDecimal precioHora, Long clubId, String clubNombre) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.precioHora = precioHora;
            this.clubId = clubId;
            this.clubNombre = clubNombre;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public BigDecimal getPrecioHora() { return precioHora; }
        public void setPrecioHora(BigDecimal precioHora) { this.precioHora = precioHora; }
        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
        public String getClubNombre() { return clubNombre; }
        public void setClubNombre(String clubNombre) { this.clubNombre = clubNombre; }
    }

    private PistaAdminDTO toDto(Pista p) {
        Long cid = p.getClub() != null ? p.getClub().getId() : null;
        String cn = p.getClub() != null ? p.getClub().getNombre() : null;
        return new PistaAdminDTO(p.getId(), p.getNombre(), p.getTipo(), p.getPrecioHora(), cid, cn);
    }

    // === Listado completo
    @GetMapping
    public List<PistaAdminDTO> listar() {
        return pistaRepository.findAll().stream().map(this::toDto).toList();
    }

    // === Detalle
    @GetMapping("/{id}")
    public ResponseEntity<PistaAdminDTO> getById(@PathVariable Long id) {
        return pistaRepository.findById(id)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // === Crear
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pista body) {
        if (body.getNombre() == null || body.getNombre().isBlank())
            return ResponseEntity.badRequest().body("El nombre es obligatorio");
        if (body.getTipo() == null || body.getTipo().isBlank())
            return ResponseEntity.badRequest().body("El tipo es obligatorio (Indoor/Outdoor)");
        if (body.getClub() == null || body.getClub().getId() == null)
            return ResponseEntity.badRequest().body("Debes indicar el club (club.id)");

        Club club = clubRepository.findById(body.getClub().getId()).orElse(null);
        if (club == null) return ResponseEntity.badRequest().body("Club no encontrado");

        Pista p = new Pista();
        p.setNombre(body.getNombre());
        p.setTipo(body.getTipo());
        p.setPrecioHora(body.getPrecioHora());
        p.setClub(club);

        Pista saved = pistaRepository.save(p);
        return ResponseEntity.ok(toDto(saved));
    }

    // === Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pista body) {
        return pistaRepository.findById(id)
                .map(p -> {
                    if (body.getNombre() != null) p.setNombre(body.getNombre());
                    if (body.getTipo() != null) p.setTipo(body.getTipo());
                    if (body.getPrecioHora() != null) p.setPrecioHora(body.getPrecioHora());
                    if (body.getClub() != null && body.getClub().getId() != null) {
                        Club c = clubRepository.findById(body.getClub().getId()).orElse(null);
                        if (c == null) return ResponseEntity.badRequest().body("Club no encontrado");
                        p.setClub(c);
                    }
                    Pista saved = pistaRepository.save(p);
                    return ResponseEntity.ok(toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === Borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!pistaRepository.existsById(id)) return ResponseEntity.notFound().build();
        pistaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
