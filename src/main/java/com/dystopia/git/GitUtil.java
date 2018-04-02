package com.dystopia.git;

import java.io.File;
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

    public static int execute(File directory, String... command) throws IOException, InterruptedException {
        return new ProcessBuilder(command).
                directory(directory).
                inheritIO().start().waitFor();
    }
}
