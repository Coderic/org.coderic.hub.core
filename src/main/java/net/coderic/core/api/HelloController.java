package net.coderic.core.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
public class HelloController {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    @GetMapping("/user")
    public ResponseEntity<OAuth2User> getUser(@AuthenticationPrincipal OAuth2User principal) {
        return new ResponseEntity<OAuth2User>(principal, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> getLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        this.logoutHandler.logout(request, response, authentication);

        return new ResponseEntity<Boolean>(true, HttpStatus.UNAUTHORIZED);
    }
}