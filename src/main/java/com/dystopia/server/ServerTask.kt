package com.dystopia.server

import com.dystopia.message.Messages
import java.net.Socket

class ServerTask(private val socket: Socket): Runnable {
    override fun run() {
        Messages.Task.parseDelimitedFrom(socket.getInputStream())
    }
}