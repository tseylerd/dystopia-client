package com.dystopia.executor;

import com.dystopia.git.CommandLineExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class StartExecutor extends TaskExecutor {
    private final File parent;

    public StartExecutor(Path file) {
        super(file);
        this.parent = file.getParent().toFile();
    }

    @Override
    public void run() {
        try {
            CommandLineExecutor.execute(parent, "git", "add", file.toAbsolutePath().toString());
            CommandLineExecutor.execute(parent, "git", "commit", "-m", "it's needed for implementation");
            CommandLineExecutor.execute(parent, "git", "stash");
            CommandLineExecutor.execute(parent, "git", "checkout", "-b", "sketch_branch", "origin/master");
            CommandLineExecutor.execute(parent, "git", "checkout", "master", file.toAbsolutePath().toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
