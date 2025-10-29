package com.tuproject.tfcback.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
    @Index(name="idx_reserva_pista_inicio", columnList = "pista_id,fechaHoraInicio"),
    @Index(name="idx_reserva_pista_fin", columnList = "pista_id,fechaHoraFin")
})
public class Reserva {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="pista_id")
    private Pista pista;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @Column(nullable=false) private LocalDateTime fechaHoraInicio;
    @Column(nullable=false) private LocalDateTime fechaHoraFin;

    private String estado = "CONFIRMADA";

    // 👇 Nuevos campos de contacto
    private String nombre;
    private String apellidos;
    private String telefono;

    // === Getters / Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pista getPista() { return pista; }
    public void setPista(Pista pista) { this.pista = pista; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }

    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
