package com.dystopia.git;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GitUtil {
    private GitUtil() {
    }

    public static boolean isUnderGit(Path file) {
        return Files.isDirectory(file.resolveSibling(".git"));
    }

    public static void ensureUnderGit(Path file) {
        if (!isUnderGit(file)) {
            throw new IllegalStateException("No git repository here.");
        }
    }

    public static int execute(Path directory, String... command) throws IOException, InterruptedException {
        String[] gitCommand = new String[command.length + 1];
        System.arraycopy(command, 0, gitCommand, 1, command.length);
        gitCommand[0] = "git";
        return new ProcessBuilder(gitCommand).
                directory(directory.toFile()).
                inheritIO().start().waitFor();
    }

    public static int executeUnderPath(Path file, String... command) throws IOException, InterruptedException {
        return execute(file.getParent(), command);
    }
}
