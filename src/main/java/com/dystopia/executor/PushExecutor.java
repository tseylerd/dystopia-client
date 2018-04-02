package com.dystopia.executor;

import com.dystopia.git.GitUtil;
import com.dystopia.server.DystopiaClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

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
        GitUtil.ensureUnderGit(file);
        try {
            GitUtil.execute(parent, "git", "fetch");
            GitUtil.execute(parent, "git", "add", file.toAbsolutePath().toString());
            GitUtil.execute(parent, "git", "commit", "-m", commitMessage);
            GitUtil.execute(parent, "git", "push", "origin", "sketch_branch:master");
        } catch (IOException | InterruptedException e) {
            DystopiaClient.LOGGER.log(Level.SEVERE, "Exception occurred while pushing " + file.getFileName(), e);
        }
    }
}
