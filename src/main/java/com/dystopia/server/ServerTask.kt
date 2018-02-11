package com.dystopia.server

import com.dystopia.executor.executorFactoryOf
import com.dystopia.message.Messages

import java.io.IOException
import java.net.Socket
import java.util.logging.Level
import java.util.logging.Logger

class ServerTask(private val mySocket: Socket) : Runnable {

    override fun run() {
        mySocket.use {
            try {
                val task = Messages.Task.parseDelimitedFrom(mySocket.getInputStream())
                executorFactoryOf(task).instanceOfExecutor().execute()
            } catch (e: IOException) {
                LOG.log(Level.WARNING, e.message)
            }
        }

    }

    companion object {
        private val LOG = Logger.getLogger("ServerTask")
    }
}
