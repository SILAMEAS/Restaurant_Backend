package com.sila.controller._public;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
@RequestMapping("/")
@Tag(name = "Public Controller", description = "Operations related to public end point use to test")
public class PublicController {
    @GetMapping
    public ResponseEntity<String> getPublic() {
        return new ResponseEntity<>("Public Api working", HttpStatus.OK);
    }
    @GetMapping("/test-api")
    public ResponseEntity<String> getTest() {
        return new ResponseEntity<>("Api working", HttpStatus.OK);
    }

}
