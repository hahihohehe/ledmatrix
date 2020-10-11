package com.github.hahihohehe.ledmatrix.server

data class Animation(var frames: List<Frame>) {
    constructor(data: Array<Array<Array<Byte>>>) : this(data.map { Frame(it) })
}