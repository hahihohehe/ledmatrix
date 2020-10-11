package com.github.hahihohehe.ledmatrix.server

data class Frame(var colors : List<Color>) {
    constructor(data: Array<Array<Byte>>) : this(data.map { Color(it[0], it[1], it[2]) })
}