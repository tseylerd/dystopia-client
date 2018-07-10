package com.dystopia.executor;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            restore(file.resolveSibling(".git"), "config");
            restore(file, ".gitattributes");
        } catch (IOException e) {
            GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while finalizing the git client: %s", e.toString());
        }
    }

    @Override
    public TaskExecutor executor() {
        return this;
    }

    private void restore(Path path, String fileName) throws IOException {
        Path resolved = path.resolve(fileName + "_copy");
        if (!Files.exists(resolved)) return;
        Files.move(resolved, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(resolved);
    }
}
