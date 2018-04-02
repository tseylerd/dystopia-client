package com.dystopia.git;

import java.nio.file.Files;
import java.nio.file.Path;

public class GitUtil {
    public static boolean isUnderGit(Path file) {
        return Files.isDirectory(file.resolveSibling(".git"));
    }

    public static void ensureUnderGit(Path file) {
        if (!isUnderGit(file)) {
            throw new IllegalStateException("No git repository here.");
        }
    }
}
