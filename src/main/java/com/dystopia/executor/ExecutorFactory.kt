package com.dystopia.executor

import com.dystopia.message.Messages

sealed class ExecutorFactory {
    abstract fun instanceOfExecutor(): TaskExecutor
}

object Pull: ExecutorFactory() {
    override fun instanceOfExecutor(): PullExecutor {
        return PullExecutor()
    }
}

object Push: ExecutorFactory() {
    override fun instanceOfExecutor(): PushExecutor {
        return PushExecutor()
    }
}

fun executorFactoryOf(task: Messages.Task): ExecutorFactory {
    return when (task.type) {
        Messages.Task.TaskType.PULL -> Pull
        Messages.Task.TaskType.PUSH -> Push
        else -> throw UnsupportedOperationException()
    }
}