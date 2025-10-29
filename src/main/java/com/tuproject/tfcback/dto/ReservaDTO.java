package com.tuproject.tfcback.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservaDTO {

    private Long id;
    private String estado;

    private Long pistaId;
    private String pistaNombre;
    private String pistaTipo;

    private String clubNombre;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private BigDecimal precioHora;

    
    private String nombre;
    private String apellidos;
    private String telefono;

    public ReservaDTO(
            Long id,
            String estado,
            Long pistaId,
            String pistaNombre,
            String pistaTipo,
            String clubNombre,
            LocalDateTime fechaHoraInicio,
            LocalDateTime fechaHoraFin,
            BigDecimal precioHora,   
            String nombre,
            String apellidos,
            String telefono
    ) {
        this.id = id;
        this.estado = estado;
        this.pistaId = pistaId;
        this.pistaNombre = pistaNombre;
        this.pistaTipo = pistaTipo;
        this.clubNombre = clubNombre;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.precioHora = precioHora;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    public ReservaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getPistaId() { return pistaId; }
    public void setPistaId(Long pistaId) { this.pistaId = pistaId; }

    public String getPistaNombre() { return pistaNombre; }
    public void setPistaNombre(String pistaNombre) { this.pistaNombre = pistaNombre; }

    public String getPistaTipo() { return pistaTipo; }
    public void setPistaTipo(String pistaTipo) { this.pistaTipo = pistaTipo; }

    public String getClubNombre() { return clubNombre; }
    public void setClubNombre(String clubNombre) { this.clubNombre = clubNombre; }

    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }

    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }

    public BigDecimal getPrecioHora() { return precioHora; }
    public void setPrecioHora(BigDecimal precioHora) { this.precioHora = precioHora; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
