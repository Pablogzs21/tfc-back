package com.tuproject.tfcback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
  @GetMapping("/whoami")
  public String whoami(org.springframework.security.core.Authentication auth) {
    if (auth == null) return "No auth";
    var roles = auth.getAuthorities().stream()
      .map(a -> a.getAuthority())
      .reduce((a,b) -> a + "," + b).orElse("");
    return "user=" + auth.getName() + " | authorities=" + roles;
  }
}
