package com.dystopia.executor;

import com.dystopia.git.CommandLineExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PushExecutor extends TaskExecutor {
    private final String commitMessage;
    private final File parent;

    public PushExecutor(Path file, String commitMessage) {
        super(file);
        this.commitMessage = commitMessage;
        parent = file.getParent().toFile();
    }

    @Override
    public void run() {
        if (!Files.isDirectory(file.resolveSibling(".git"))) {
            System.err.println("No git repo here");
            return;
        }
        try {
            CommandLineExecutor.execute(parent, "git", "fetch");
            CommandLineExecutor.execute(parent, "git", "add", file.toAbsolutePath().toString());
            CommandLineExecutor.execute(parent, "git", "commit", "-m", commitMessage);
            CommandLineExecutor.execute(parent, "git", "push", "origin", "sketch_branch:master");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
