package com.dystopia.server;

import com.dystopia.executor.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArgumentsParser {
    private static final String USAGE = "Usage: <action> <file_name> [commit_message]";

    public static TaskExecutor parse(String[] args) throws IllegalArgumentException {
        if (args.length < 2) {
            throw new IllegalArgumentException(USAGE);
        }

        Path path = Paths.get(args[1]);
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("File " + args[1] + " does not exists");
        }
        switch (args[0]) {
            case "stop": {
                return new StopExecutor(path);
            }
            case "start": {
                return new StartExecutor(path);
            }
            case "push": {
                if (args.length < 3 || args[2].isEmpty()) {
                    throw new IllegalArgumentException(USAGE);
                }
                return new PushExecutor(path, args[2]);
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
