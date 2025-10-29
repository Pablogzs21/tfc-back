package com.tuproject.tfcback.dto;

import java.math.BigDecimal;

public class PistaAdminDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private BigDecimal precioHora;
    private Long clubId;
    private String clubNombre;

    public PistaAdminDTO(Long id, String nombre, String tipo, BigDecimal precioHora,
                         Long clubId, String clubNombre) {
        this.id = id; this.nombre = nombre; this.tipo = tipo;
        this.precioHora = precioHora; this.clubId = clubId; this.clubNombre = clubNombre;
    }
    public PistaAdminDTO() {}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public BigDecimal getPrecioHora() {
		return precioHora;
	}
	public void setPrecioHora(BigDecimal precioHora) {
		this.precioHora = precioHora;
	}
	public Long getClubId() {
		return clubId;
	}
	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}
	public String getClubNombre() {
		return clubNombre;
	}
	public void setClubNombre(String clubNombre) {
		this.clubNombre = clubNombre;
	}
    
}
