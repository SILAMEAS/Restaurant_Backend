package com.sila.controller.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-api")
public class TestController {
  @GetMapping()
  public ResponseEntity<String> getTest(){
    return new ResponseEntity<>("Api working", HttpStatus.OK);
  }

}
