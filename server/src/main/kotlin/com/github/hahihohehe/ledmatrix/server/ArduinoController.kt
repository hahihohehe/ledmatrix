package com.github.hahihohehe.ledmatrix.server

import com.fazecast.jSerialComm.SerialPort

class ArduinoController {
    private val arduinoPorts = SerialPort.getCommPorts().filter { it.systemPortName.contains("ttyACM") }

    init {
        arduinoPorts.forEach {
            it.openPort()
            it.baudRate = 9600
            Thread.sleep(2000)
        }
    }

    fun sendFrame(frame: Frame) {
        arduinoPorts.forEach {
            val bytes = frame.getBytes()
            it.writeBytes(bytes, bytes.size.toLong())
        }
    }
}