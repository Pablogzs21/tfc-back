package com.tuproject.tfcback.repository;

import com.tuproject.tfcback.dto.PistaDTO;
import com.tuproject.tfcback.model.Pista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PistaRepository extends JpaRepository<Pista, Long> {

    @Query("""
        SELECT new com.tuproject.tfcback.dto.PistaDTO(
            p.id, p.nombre, p.tipo, p.precioHora,
            p.club.id, p.club.nombre,
            p.club.direccion,           
            p.club.lat,                 
            p.club.lng                   
        )
        FROM Pista p
        ORDER BY p.club.nombre ASC, p.nombre ASC
    """)
    List<PistaDTO> findAllDto();

    @Query("""
        SELECT new com.tuproject.tfcback.dto.PistaDTO(
            p.id, p.nombre, p.tipo, p.precioHora,
            p.club.id, p.club.nombre,
            p.club.direccion,
            p.club.lat,
            p.club.lng
        )
        FROM Pista p
        WHERE p.id = :id
    """)
    Optional<PistaDTO> findDtoById(@Param("id") Long id);

    @Query("""
        SELECT new com.tuproject.tfcback.dto.PistaDTO(
            p.id, p.nombre, p.tipo, p.precioHora,
            p.club.id, p.club.nombre,
            p.club.direccion,
            p.club.lat,
            p.club.lng
        )
        FROM Pista p
        WHERE p.club.id = :clubId
        ORDER BY p.nombre ASC
    """)
    List<PistaDTO> findDtoByClubId(@Param("clubId") Long clubId);

    @Query("""
        SELECT new com.tuproject.tfcback.dto.PistaDTO(
            p.id, p.nombre, p.tipo, p.precioHora,
            p.club.id, p.club.nombre,
            p.club.direccion,
            p.club.lat,
            p.club.lng
        )
        FROM Pista p
        WHERE LOWER(p.tipo) = LOWER(:tipo)
        ORDER BY p.club.nombre ASC, p.nombre ASC
    """)
    List<PistaDTO> findDtoByTipo(@Param("tipo") String tipo);

    @Query("""
        SELECT new com.tuproject.tfcback.dto.PistaDTO(
            p.id, p.nombre, p.tipo, p.precioHora,
            p.club.id, p.club.nombre,
            p.club.direccion,
            p.club.lat,
            p.club.lng
        )
        FROM Pista p
        WHERE (:tipo IS NULL OR LOWER(p.tipo) = LOWER(:tipo))
          AND (:clubId IS NULL OR p.club.id = :clubId)
        ORDER BY p.club.nombre ASC, p.nombre ASC
    """)
    List<PistaDTO> findByFilters(@Param("tipo") String tipo, @Param("clubId") Long clubId);
}
