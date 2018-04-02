package com.dystopia.executor;

import com.dystopia.Definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class StopExecutor implements TaskExecutor, TaskDefinition {
    private final Path file;

    public StopExecutor(Path file) {
        this.file = file;
    }

    @Override
    public void execute() {
        GitUtil.ensureUnderGit(file);
        try {
            GitUtil.executeUnderPath(file, "checkout", "master");
            GitUtil.executeUnderPath(file, "stash", "apply");
            GitUtil.executeUnderPath(file, "reset", "--soft", "HEAD~1");
            GitUtil.executeUnderPath(file, "checkout", "sketch_branch", file.toAbsolutePath().toString());
            GitUtil.executeUnderPath(file, "branch", "-fd", "sketch_branch");
        } catch (IOException | InterruptedException e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while finalizing the git client", e);
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
