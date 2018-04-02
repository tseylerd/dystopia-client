package com.dystopia.server;

import com.dystopia.executor.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DystopiaClient {
    public static final Logger LOGGER = Logger.getLogger("dystopia-client");
    public static void main(String[] args) {
        try {
            TaskExecutor taskExecutor = ArgumentsParser.parse(args);
            taskExecutor.run();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Exception occurred while parsing arguments", e);
        }
    }
}

