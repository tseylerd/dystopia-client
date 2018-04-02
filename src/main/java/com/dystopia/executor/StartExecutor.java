package com.dystopia.executor;

import com.dystopia.git.GitUtil;
import com.dystopia.server.DystopiaClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class StartExecutor extends TaskExecutor {
    private final File parent;

    public StartExecutor(Path file) {
        super(file);
        this.parent = file.getParent().toFile();
    }

    @Override
    public void run() {
        try {
            GitUtil.execute(parent, "git", "add", file.toAbsolutePath().toString());
            GitUtil.execute(parent, "git", "commit", "-m", "it's needed for implementation");
            GitUtil.execute(parent, "git", "stash");
            GitUtil.execute(parent, "git", "checkout", "-b", "sketch_branch", "origin/master");
            GitUtil.execute(parent, "git", "checkout", "master", file.toAbsolutePath().toString());
        } catch (IOException | InterruptedException e) {
            DystopiaClient.LOGGER.log(Level.SEVERE, "Exception occurred while initializing the git client", e);
        }
    }
}
