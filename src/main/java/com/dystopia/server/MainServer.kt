package com.dystopia.server

import java.net.ServerSocket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainServer(port: Int) {

    companion object {
        private val executor: Executor = Executors.newSingleThreadExecutor()
    }

    private val socket: ServerSocket = ServerSocket(port)

    fun start() {
        while (true) {
            val accepted = socket.accept()
            executor.execute(ServerTask(accepted))
        }
    }
}