package com.epam.esm.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/testing")
public class TestController {

    @GetMapping
    public String getString(){
        return "working!";
    }

    @GetMapping("/forbidden")
    public String getForbidden(){
        return "forbidden";
    }
}
