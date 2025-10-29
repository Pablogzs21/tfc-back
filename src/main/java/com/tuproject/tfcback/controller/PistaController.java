package com.tuproject.tfcback.controller;

import com.tuproject.tfcback.dto.PistaDTO;
import com.tuproject.tfcback.repository.PistaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pistas")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PistaController {

    private final PistaRepository pistaRepository;

    public PistaController(PistaRepository pistaRepository) {
        this.pistaRepository = pistaRepository;
    }
    @GetMapping
    public ResponseEntity<List<PistaDTO>> listarFiltrado(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Long clubId
    ) {
        if (tipo == null && clubId == null) {
            return ResponseEntity.ok(pistaRepository.findAllDto());
        }
        return ResponseEntity.ok(pistaRepository.findByFilters(tipo, clubId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PistaDTO> getById(@PathVariable Long id) {
        return pistaRepository.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
