package com.tuproject.tfcback.dto;

import java.math.BigDecimal;

public record PistaDTO(
    Long id,
    String nombre,
    String tipo,
    BigDecimal precioHora,
    Long clubId,
    String clubNombre,
    String clubDireccion, 
    Double clubLat,       
    Double clubLng        
) {}
