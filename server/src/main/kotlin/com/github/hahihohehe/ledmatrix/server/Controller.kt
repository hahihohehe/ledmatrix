package com.github.hahihohehe.ledmatrix.server

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
    @RequestMapping("/test")
    fun test(): Array<Array<Array<Byte>>> {
        return arrayOf(arrayOf(arrayOf<Byte>(1,2,3), arrayOf<Byte>(4,5,6), arrayOf<Byte>(7,8,9)))
    }

    @PostMapping("/display")
    fun display(@RequestBody data : Array<Array<Array<Byte>>>) : String {
        var animation = Animation(data)
        return "OK"
    }
}