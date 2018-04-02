package com.dystopia.executor;

import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class PushExecutor extends TaskExecutor {
    private final String commitMessage;

    public PushExecutor(Path file, String commitMessage) {
        super(file);
        this.commitMessage = commitMessage;
    }

    @Override
    public void run() {
        GitUtil.ensureUnderGit(file);
        try {
            GitUtil.executeUnderPath(file, "fetch");
            GitUtil.executeUnderPath(file, "add", file.toAbsolutePath().toString());
            GitUtil.executeUnderPath(file, "commit", "-m", commitMessage);
            GitUtil.executeUnderPath(file, "push", "origin", "sketch_branch:master");
        } catch (IOException | InterruptedException e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while pushing " + file.getFileName(), e);
        }
    }
}
