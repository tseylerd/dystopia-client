package com.dystopia.server;

import com.dystopia.executor.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ArgumentsParser {
    private static final String USAGE = "Usage: <action> <file_name> [commit_message]";
    private static final int ACTION = 0;
    private static final int FILE = 1;
    private static final int COMMIT_MESSAGE = 2;

    private ArgumentsParser() {

    }

    public static TaskExecutor parse(String[] args) throws IllegalArgumentException {
        if (args.length < 2) {
            throw new IllegalArgumentException(USAGE);
        }

        Path path = Paths.get(args[FILE]);
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("File " + args[FILE] + " does not exists");
        }
        switch (args[ACTION]) {
            case "stop": {
                return new StopExecutor(path);
            }
            case "start": {
                return new StartExecutor(path);
            }
            case "push": {
                if (args.length < 3 || args[COMMIT_MESSAGE].isEmpty()) {
                    throw new IllegalArgumentException(USAGE);
                }
                return new PushExecutor(path, args[COMMIT_MESSAGE]);
            }
            case "pull": {
                return new PullExecutor(path);
            }
            default: {
                throw new IllegalArgumentException(USAGE);
            }
        }
    }
}
