package com.dystopia.git;

import java.io.File;
import java.io.IOException;

public class CommandLineExecutor {
    public static int execute(File directory, String... command) throws IOException, InterruptedException {
        return new ProcessBuilder(command).
                directory(directory).
                inheritIO().start().waitFor();
    }
}
