package com.dystopia.git;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GitUtil {
    private GitUtil() {
    }

    private static boolean isUnderGit(Path file) {
        return Files.isDirectory(file.resolveSibling(".git"));
    }

    public static void ensureUnderGit(Path file) {
        if (!isUnderGit(file)) {
            throw new IllegalStateException("No git repository here.");
        }
    }

    public static void execute(Path directory, String... command) throws IOException, InterruptedException {
        String[] args = new String[command.length + 1];
        System.arraycopy(command, 0, args, 1, command.length);
        args[0] = "git";
        new ProcessBuilder(args).
          directory(directory.toFile()).
          inheritIO().start().waitFor();
    }
}
