package com.dystopia.executor;

import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class StopExecutor extends TaskExecutor {

    public StopExecutor(Path file) {
        super(file);
    }

    @Override
    public void run() {
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
}
