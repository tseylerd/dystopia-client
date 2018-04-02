package com.dystopia.executor;

import com.dystopia.Definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class StartExecutor implements TaskExecutor, TaskDefinition {
    private final Path file;

    public StartExecutor(Path file) {
        this.file = file;
    }

    @Override
    public void execute() {
        try {
            GitUtil.executeUnderPath(file, "add", file.toAbsolutePath().toString());
            GitUtil.executeUnderPath(file, "commit", "-m", "it's needed for implementation");
            GitUtil.executeUnderPath(file, "stash");
            GitUtil.executeUnderPath(file, "checkout", "-b", "sketch_branch", "origin/master");
            GitUtil.executeUnderPath(file, "checkout", "master", file.toAbsolutePath().toString());
        } catch (IOException | InterruptedException e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while initializing the git client", e);
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
