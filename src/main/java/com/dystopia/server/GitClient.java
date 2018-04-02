package com.dystopia.server;

import com.dystopia.executor.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GitClient {
    public static final Logger LOGGER = Logger.getLogger("GitClient");

    public static void main(String[] args) {
        try {
            TaskExecutor taskExecutor = ArgumentsParser.parse(args);
            taskExecutor.execute();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while parsing arguments", e);
        }
    }
}

