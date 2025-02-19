package org.coderic.hub.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sandbox {
    @GetMapping("hello")
    public ResponseEntity<String> index() {
        return new ResponseEntity<String>("Hello World", HttpStatus.OK);
    }
}
