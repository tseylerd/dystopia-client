package com.dystopia.executor;

import com.dystopia.git.CommandLineExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PullExecutor extends TaskExecutor {
    public PullExecutor(Path file) {
        super(file);
    }

    @Override
    public void run() {
        if (!Files.isDirectory(file.resolveSibling(".git"))) {
            System.err.println("No git repo here");
            return;
        }
        try {
            CommandLineExecutor.execute(file.getParent().toFile(), "git", "fetch");
            CommandLineExecutor.execute(file.getParent().toFile(), "git", "checkout", "origin/master",
                    file.toAbsolutePath().toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
