package com.github.hahihohehe.ledmatrix.server

data class Color(var red : Byte, var green : Byte, var blue : Byte) {
    constructor(data : Array<Byte>) : this(data[0], data[1], data[2])
}
