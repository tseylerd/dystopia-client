package com.dystopia.executor;

import com.dystopia.Definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class PushExecutor implements TaskExecutor, TaskDefinition {
    private final Path file;
    private final String commitMessage;

    public PushExecutor(Path file, String commitMessage) {
        this.file = file;
        this.commitMessage = commitMessage;
    }

    @Override
    public void execute() {
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

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
