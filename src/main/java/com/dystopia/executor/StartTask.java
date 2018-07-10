package com.dystopia.executor;

import com.dystopia.Util;
import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class StartTask implements TaskExecutor, TaskDefinition {
  private final Path file;

  public StartTask(Path file) {
    this.file = file;
  }

  @Override
  public void execute() {
    try {
      Path gitDirectory = file.resolveSibling(".git");
      copyAndModify(gitDirectory, "config", path -> {
        Path currentJarPath = Util.getContainingPath();
        Path driverRunnerPath = currentJarPath.resolveSibling("run-driver.sh");
        Files.write(
          path,
          Arrays.asList(
            "",
            "[merge \"dystopia\"]",
            "\tname = dystopia",
            "\tdriver = /bin/sh '" + driverRunnerPath.toFile().getPath() + "' %O %A %B"
          ),
          StandardOpenOption.APPEND,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE
        );
      });

      copyAndModify(file, ".gitattributes", path -> {
        Files.write(
          path,
          Collections.singletonList("*.sketch merge=dystopia"),
          StandardOpenOption.APPEND,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE
        );
      });

      GitUtil.executeUnderPathOrExit(file, "add", file.toAbsolutePath().toString());
    } catch (IOException | InterruptedException e) {
      GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while initializing the git client: %s", e.toString());
    }
  }

  @Override
  public TaskExecutor executor() {
    return this;
  }

  private void copyAndModify(Path rootPath, String fileName, FileOperation operation) throws IOException {
    Path file = rootPath.resolve(fileName);
    boolean exists = Files.exists(file);
    List<String> strings = exists ? Files.readAllLines(file) : Collections.emptyList();
    if (strings.stream().noneMatch(s -> s.contains("dystopia"))) {
      if (exists) {
        Path configCopy = file.resolveSibling(String.format("%s_copy", fileName));
        Files.copy(file, configCopy, StandardCopyOption.REPLACE_EXISTING);
      }
      operation.perform(file);
    }
  }

  private interface FileOperation {
    void perform(Path path) throws IOException;
  }
}
