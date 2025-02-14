package org.coderic.hub.core.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "¡Acceso permitido! Este endpoint está protegido por OAuth2.";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Este endpoint es público.";
    }
}