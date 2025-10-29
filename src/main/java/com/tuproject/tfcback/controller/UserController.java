package com.tuproject.tfcback.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/data")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userData() {
        return "📦 Datos visibles para usuarios autenticados (USER o ADMIN)";
    }

    @GetMapping("/admin/data")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminData() {
        return "👑 Datos solo para ADMINISTRADORES";
    }
}
