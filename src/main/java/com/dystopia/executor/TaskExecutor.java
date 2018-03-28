package com.dystopia.executor;

import java.nio.file.Path;

public abstract class TaskExecutor implements Runnable {
    protected final Path file;

    protected TaskExecutor(Path file) {
        this.file = file;
    }
}
