package com.dystopia.executor;

import com.dystopia.git.GitUtil;
import com.dystopia.server.DystopiaClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public class StopExecutor extends TaskExecutor {
    private final File parent;

    public StopExecutor(Path file) {
        super(file);
        this.parent = file.getParent().toFile();
    }

    @Override
    public void run() {
        GitUtil.ensureUnderGit(file);
        try {
            GitUtil.execute(parent, "git", "checkout", "master");
            GitUtil.execute(parent, "git", "stash", "apply");
            GitUtil.execute(parent, "git", "reset", "--soft", "HEAD~1");
            GitUtil.execute(parent, "git", "checkout", "sketch_branch", file.toAbsolutePath().toString());
            GitUtil.execute(parent, "git", "branch", "-fd", "sketch_branch");
        } catch (IOException | InterruptedException e) {
            DystopiaClient.LOGGER.log(Level.SEVERE, "Exception occurred while finalizing the git client", e);
        }
    }
}
