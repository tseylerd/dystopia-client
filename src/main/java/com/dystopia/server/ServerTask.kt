package com.dystopia.server

import com.dystopia.message.Messages

import java.io.IOException
import java.net.Socket
import java.util.logging.Level
import java.util.logging.Logger

class ServerTask(private val mySocket: Socket) : Runnable {

    override fun run() {
        try {
            Messages.Task.parseDelimitedFrom(mySocket.getInputStream())
        } catch (e: IOException) {
            LOG.log(Level.WARNING, e.message)
        }

    }

    companion object {
        private val LOG = Logger.getLogger("ServerTask")
    }
}
