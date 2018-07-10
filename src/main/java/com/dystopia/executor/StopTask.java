package com.dystopia.executor;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class StopTask implements TaskExecutor, TaskDefinition {
    private final Path file;

    public StopTask(Path file) {
        this.file = file;
    }

    @Override
    public void execute() {
        GitUtil.ensureUnderGit(file);
        try {
            Path configPath = file.resolveSibling(".git").resolve("config");
            Path copyConfigPath = file.resolveSibling("copyConfig");
            if (Files.isRegularFile(copyConfigPath)) {
                Files.move(copyConfigPath, configPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Path gitattributesPath = file.resolveSibling(".gitattributes");
            Path copyGitattributesPath = file.resolveSibling(".copygitattributes");
            if (Files.isRegularFile(copyGitattributesPath)) {
                Files.move(copyGitattributesPath, gitattributesPath, StandardCopyOption.REPLACE_EXISTING);
            }

            GitUtil.executeUnderPathOrExit(file, "checkout", "master");
            GitUtil.executeUnderPathOrExit(file, "stash", "apply");
            GitUtil.executeUnderPathOrExit(file, "reset", "--soft", "HEAD~1");
            GitUtil.executeUnderPathOrExit(file, "checkout", "sketch_branch", file.toAbsolutePath().toString());
            GitUtil.executeUnderPathOrExit(file, "branch", "-fd", "sketch_branch");
        } catch (IOException | InterruptedException e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while finalizing the git client: %s", e.toString());
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }
}
