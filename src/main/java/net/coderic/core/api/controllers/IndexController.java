package net.coderic.core.api.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model data, @AuthenticationPrincipal OAuth2User principal) {

        data.addAttribute("principal",principal);
        return "home";
    }
}
