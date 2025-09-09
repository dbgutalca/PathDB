package com.gdblab.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class SanityController {
    
    @GetMapping("/")
    public String sanityCheck() {
        return "Working!";
    }

}
