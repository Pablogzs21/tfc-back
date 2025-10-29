package com.tuproject.tfcback.controller;

import com.tuproject.tfcback.dto.ReservaDTO;
import com.tuproject.tfcback.dto.ReservaRequest;
import com.tuproject.tfcback.model.Pista;
import com.tuproject.tfcback.model.Reserva;
import com.tuproject.tfcback.model.Usuario;
import com.tuproject.tfcback.repository.PistaRepository;
import com.tuproject.tfcback.repository.ReservaRepository;
import com.tuproject.tfcback.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PistaRepository pistaRepository;

    private static final LocalTime CLUB_OPEN = LocalTime.of(8, 0);
    private static final LocalTime CLUB_CLOSE = LocalTime.of(23, 0);

    public ReservaController(ReservaRepository reservaRepository,
                             UsuarioRepository usuarioRepository,
                             PistaRepository pistaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.pistaRepository = pistaRepository;
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) return null;
        return usuarioRepository.findByUsername(auth.getName())
                .map(Usuario::getId)
                .orElse(null);
    }

    @GetMapping("/mias")
    public ResponseEntity<List<ReservaDTO>> mias() {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(reservaRepository.findDtoByUsuario(userId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> porUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reservaRepository.findDtoByUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> get(@PathVariable Long id) {
        return reservaRepository.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> crear(@RequestBody ReservaRequest req) {
        if (req.getPistaId() == null || req.getFechaHoraInicio() == null || req.getFechaHoraFin() == null) {
            return ResponseEntity.badRequest().body("Datos incompletos");
        }
        if (!req.getFechaHoraFin().isAfter(req.getFechaHoraInicio())) {
            return ResponseEntity.badRequest().body("El fin debe ser posterior al inicio");
        }

        LocalDate hoy = LocalDate.now();
        if (req.getFechaHoraInicio().toLocalDate().isBefore(hoy) || req.getFechaHoraFin().toLocalDate().isBefore(hoy)) {
            return ResponseEntity.badRequest().body("No se puede reservar en días anteriores al de hoy");
        }

        long minutos = Duration.between(req.getFechaHoraInicio(), req.getFechaHoraFin()).toMinutes();
        if (minutos < 30)
            return ResponseEntity.badRequest().body("La duración mínima de una reserva es de 30 minutos");
        if (minutos % 30 != 0)
            return ResponseEntity.badRequest().body("La duración debe ser en múltiplos de 30 minutos (30, 60, 90, ...)");

        if (!isWithinClubHours(req.getFechaHoraInicio()) || !isWithinClubHours(req.getFechaHoraFin())) {
            return ResponseEntity.badRequest().body("El club solo permite reservas entre las 08:00 y las 23:00");
        }

        Long usuarioId = req.getUsuarioId() != null ? req.getUsuarioId() : getAuthenticatedUserId();
        if (usuarioId == null) return ResponseEntity.status(401).body("Usuario no autenticado");

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Pista> pistaOpt = pistaRepository.findById(req.getPistaId());
        if (usuarioOpt.isEmpty() || pistaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario o pista inexistente");
        }

        boolean libre = reservaRepository
                .findSolapadas(req.getPistaId(), req.getFechaHoraInicio(), req.getFechaHoraFin())
                .isEmpty();
        if (!libre) {
            return ResponseEntity.status(409).body("La pista no está disponible en ese horario");
        }

        Reserva r = new Reserva();
        r.setUsuario(usuarioOpt.get());
        r.setPista(pistaOpt.get());
        r.setFechaHoraInicio(req.getFechaHoraInicio());
        r.setFechaHoraFin(req.getFechaHoraFin());
        r.setEstado("CONFIRMADA");
        r.setNombre(req.getNombre());
        r.setApellidos(req.getApellidos());
        r.setTelefono(req.getTelefono());

        r = reservaRepository.save(r);

        ReservaDTO dto = reservaRepository.findDtoById(r.getId())
                .orElseThrow(() -> new RuntimeException("No se pudo cargar la reserva creada"));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> modificar(@PathVariable Long id, @RequestBody ReservaRequest req) {
        if (req.getPistaId() == null || req.getFechaHoraInicio() == null || req.getFechaHoraFin() == null) {
            return ResponseEntity.badRequest().body("Datos incompletos");
        }
        if (!req.getFechaHoraFin().isAfter(req.getFechaHoraInicio())) {
            return ResponseEntity.badRequest().body("El fin debe ser posterior al inicio");
        }

        LocalDate hoy = LocalDate.now();
        if (req.getFechaHoraInicio().toLocalDate().isBefore(hoy) || req.getFechaHoraFin().toLocalDate().isBefore(hoy)) {
            return ResponseEntity.badRequest().body("No se puede reservar en días anteriores al de hoy");
        }

        long minutos = Duration.between(req.getFechaHoraInicio(), req.getFechaHoraFin()).toMinutes();
        if (minutos < 30)
            return ResponseEntity.badRequest().body("La duración mínima de una reserva es de 30 minutos");
        if (minutos % 30 != 0)
            return ResponseEntity.badRequest().body("La duración debe ser en múltiplos de 30 minutos (30, 60, 90, ...)");

        if (!isWithinClubHours(req.getFechaHoraInicio()) || !isWithinClubHours(req.getFechaHoraFin())) {
            return ResponseEntity.badRequest().body("El club solo permite reservas entre las 08:00 y las 23:00");
        }

        Reserva r = reservaRepository.findById(id).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();

        Long userId = getAuthenticatedUserId();
        if (userId == null || !r.getUsuario().getId().equals(userId)) {
            return ResponseEntity.status(403).body("No puedes modificar una reserva de otro usuario");
        }

        if (!"CONFIRMADA".equalsIgnoreCase(r.getEstado())) {
            return ResponseEntity.status(409).body("Solo se pueden modificar reservas CONFIRMADAS");
        }

        Pista pista = pistaRepository.findById(req.getPistaId()).orElse(null);
        if (pista == null) return ResponseEntity.badRequest().body("Pista inexistente");

        boolean libre = reservaRepository
                .findSolapadasExcepto(id, req.getPistaId(), req.getFechaHoraInicio(), req.getFechaHoraFin())
                .isEmpty();
        if (!libre) {
            return ResponseEntity.status(409).body("La pista no está disponible en ese horario");
        }

        r.setPista(pista);
        r.setFechaHoraInicio(req.getFechaHoraInicio());
        r.setFechaHoraFin(req.getFechaHoraFin());

        if (req.getNombre() != null)    r.setNombre(req.getNombre());
        if (req.getApellidos() != null) r.setApellidos(req.getApellidos());
        if (req.getTelefono() != null)  r.setTelefono(req.getTelefono());

        reservaRepository.save(r);

        ReservaDTO dto = reservaRepository.findDtoById(r.getId())
                .orElseThrow(() -> new RuntimeException("No se pudo cargar la reserva modificada"));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        Optional<Reserva> rOpt = reservaRepository.findById(id);
        if (rOpt.isEmpty()) return ResponseEntity.notFound().build();

        Reserva r = rOpt.get();
        Long userId = getAuthenticatedUserId();
        if (userId == null || !r.getUsuario().getId().equals(userId)) {
            return ResponseEntity.status(403).body("No puedes cancelar una reserva de otro usuario");
        }

        r.setEstado("CANCELADA");
        reservaRepository.save(r);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @Transactional
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        Optional<Reserva> rOpt = reservaRepository.findById(id);
        if (rOpt.isEmpty()) return ResponseEntity.notFound().build();

        Reserva r = rOpt.get();
        Long userId = getAuthenticatedUserId();
        if (userId == null || !r.getUsuario().getId().equals(userId)) {
            return ResponseEntity.status(403).body("No puedes borrar una reserva de otro usuario");
        }

        if (!"CANCELADA".equalsIgnoreCase(r.getEstado())) {
            return ResponseEntity.status(409).body("Solo se pueden borrar reservas CANCELADAS");
        }

        reservaRepository.delete(r);
        return ResponseEntity.noContent().build();
    }

    private boolean isWithinClubHours(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        return !time.isBefore(CLUB_OPEN) && !time.isAfter(CLUB_CLOSE);
    }
}
