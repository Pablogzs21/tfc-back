package com.tuproject.tfcback.repository;

import com.tuproject.tfcback.dto.ReservaDTO;
import com.tuproject.tfcback.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Mis reservas
    @Query("""
           select new com.tuproject.tfcback.dto.ReservaDTO(
               r.id, r.estado,
               p.id, p.nombre, p.tipo,
               c.nombre,
               r.fechaHoraInicio, r.fechaHoraFin,
               p.precioHora,                
               r.nombre, r.apellidos, r.telefono
           )
           from Reserva r
           join r.pista p
           join p.club c
           where r.usuario.id = :usuarioId
           order by r.fechaHoraInicio desc
           """)
    List<ReservaDTO> findDtoByUsuario(@Param("usuarioId") Long usuarioId);

    // Una reserva por id
    @Query("""
           select new com.tuproject.tfcback.dto.ReservaDTO(
               r.id, r.estado,
               p.id, p.nombre, p.tipo,
               c.nombre,
               r.fechaHoraInicio, r.fechaHoraFin,
               p.precioHora,                
               r.nombre, r.apellidos, r.telefono
           )
           from Reserva r
           join r.pista p
           join p.club c
           where r.id = :id
           """)
    Optional<ReservaDTO> findDtoById(@Param("id") Long id);

    // Solapadas (tal y como ya las tienes)
    @Query("""
           select r from Reserva r
           where r.pista.id = :pistaId
             and r.estado = 'CONFIRMADA'
             and r.fechaHoraInicio < :fin
             and r.fechaHoraFin > :inicio
           """)
    List<Reserva> findSolapadas(@Param("pistaId") Long pistaId,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fin") LocalDateTime fin);

    @Query("""
           select r from Reserva r
           where r.pista.id = :pistaId
             and r.id <> :reservaId
             and r.estado = 'CONFIRMADA'
             and r.fechaHoraInicio < :fin
             and r.fechaHoraFin > :inicio
           """)
    List<Reserva> findSolapadasExcepto(@Param("reservaId") Long reservaId,
                                       @Param("pistaId") Long pistaId,
                                       @Param("inicio") LocalDateTime inicio,
                                       @Param("fin") LocalDateTime fin);
}
