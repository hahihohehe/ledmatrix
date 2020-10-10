package com.github.hahihohehe.ledmatrix.server

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
    @RequestMapping("/hello")
    fun index(): String {
        return "Greetings from Spring Boot!"
    }


    @PostMapping("/display")
    fun display(animation : Animation) : String {
        return "OK"
    }
}