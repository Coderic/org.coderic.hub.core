package org.coderic.hub.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    @GetMapping("/user")
    public ResponseEntity<Principal> getUser(Principal principal) {
        return new ResponseEntity<Principal>(principal, HttpStatus.OK);
    }
    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "Hello, " + principal.getName() + "!";
    }
}