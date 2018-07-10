package com.dystopia.executor;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class PullTask implements TaskExecutor, TaskDefinition {
    private final Path file;

    public PullTask(Path file) {
        this.file = file;
    }

    @Override
    public void execute() {
        GitUtil.ensureUnderGit(file);
        try {
            GitClient.LOGGER.log(Level.SEVERE, "fetch");
            GitUtil.executeUnderPathOrExit(file, "fetch");
            GitClient.LOGGER.log(Level.SEVERE, "merge");
            GitUtil.executeUnderPathOrExit(file, "merge", "origin/master");
        } catch (Throwable e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while pulling from origin/master: %s", e.toString());
            GitClient.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
