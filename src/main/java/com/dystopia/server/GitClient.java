package com.dystopia.server;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.executor.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GitClient {
    public static final Logger LOGGER = Logger.getLogger("GitClient");

    public static void main(String[] args) {
        try {
            for (String arg: args) {
                LOGGER.log(Level.SEVERE, arg);
            }
            TaskDefinition task = ArgumentsParser.parse(args);
            task.executor().execute();
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, e, e::getMessage);
        }
    }
}

