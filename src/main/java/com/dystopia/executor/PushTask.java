package com.dystopia.executor;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class PushTask implements TaskExecutor, TaskDefinition {
    private final Path file;
    private final String commitMessage;

    public PushTask(Path file, String commitMessage) {
        this.file = file;
        this.commitMessage = commitMessage;
    }

    @Override
    public void execute() {
        GitClient.LOGGER.log(Level.SEVERE, file.toString());
        GitClient.LOGGER.log(Level.SEVERE, "ensuring");
        GitUtil.ensureUnderGit(file);
        try {
            GitClient.LOGGER.log(Level.SEVERE, "fetching");
            GitUtil.executeUnderPathOrExit(file, "fetch");
            GitClient.LOGGER.log(Level.SEVERE, "adding");
            GitUtil.executeUnderPathOrExit(file, "add", file.toAbsolutePath().toString());
            GitClient.LOGGER.log(Level.SEVERE, "commiting");
            GitUtil.executeUnderPathOrExit(file, "commit", "-m", commitMessage);
            GitClient.LOGGER.log(Level.SEVERE, "pushing");
            GitUtil.executeUnderPathOrExit(file, "push", "origin", "sketch_branch:master");
        } catch (Throwable e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while pushing " + file.getFileName() + ": %s", e.toString());
            GitClient.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
