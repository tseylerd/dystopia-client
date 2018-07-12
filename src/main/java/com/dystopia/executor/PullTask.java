package com.dystopia.executor;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

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
            GitUtil.execute(file.getParent(), "add", file.toAbsolutePath().toString());
            GitUtil.execute(file.getParent(), "commit", "-m", "temporary commit");
            GitClient.LOGGER.log(Level.SEVERE, "fetching");
            GitUtil.execute(file.getParent(), "fetch");
            GitClient.LOGGER.log(Level.SEVERE, "merging");
            GitUtil.execute(file.getParent(), "merge");
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
