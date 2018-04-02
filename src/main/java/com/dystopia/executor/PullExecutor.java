package com.dystopia.executor;

import com.dystopia.Definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class PullExecutor implements TaskExecutor, TaskDefinition {
    private final Path file;

    public PullExecutor(Path file) {
        this.file = file;
    }

    @Override
    public void execute() {
        GitUtil.ensureUnderGit(file);
        try {
            GitUtil.executeUnderPath(file, "fetch");
            GitUtil.executeUnderPath(file, "checkout", "origin/master", file.toAbsolutePath().toString());
        } catch (IOException | InterruptedException e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while pulling from origin/master", e);
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
