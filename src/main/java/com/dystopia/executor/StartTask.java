package com.dystopia.executor;

import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class StartTask implements TaskExecutor, TaskDefinition {
  private final Path file;

  public StartTask(Path file) {
    this.file = file;
  }

  @Override
  public void execute() {
    try {
      Path config = file.resolveSibling(".git").resolve("config");
      List<String> strings = Files.exists(config) ? Files.readAllLines(config) : Collections.emptyList();
      if (strings.stream().noneMatch(s -> s.contains("dystopia"))) {
        URL location = StartTask.class.getProtectionDomain().getCodeSource().getLocation();
        String path = location.getPath();
        Path myPath = Paths.get(path);

        Path driverRunnerPath = myPath.resolveSibling("run-driver.sh");

        Files.write(
          config,
          Arrays.asList(
            "",
            "[merge \"dystopia-driver\"]",
            "\tname = dystopia-driver",
            "\tdriver = /bin/sh '" + driverRunnerPath.toFile().getPath() + "' %O %A %B"
          ),
          StandardOpenOption.APPEND,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE
        );
      }

      Path attributes = file.resolveSibling(".gitattributes");
      strings = Files.exists(attributes) ? Files.readAllLines(attributes) : Collections.emptyList();

      if (strings.stream().noneMatch(s -> s.contains("dystopia"))) {
        Files.write(
          attributes,
          Collections.singletonList("*.sketch merge=dystopia-driver"),
          StandardOpenOption.APPEND,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE
        );
      }

      GitUtil.executeUnderPathOrExit(file, "add", file.toAbsolutePath().toString());
      GitUtil.executeUnderPathOrExit(file, "commit", "--allow-empty", "-m", "it's needed for implementation");
      GitUtil.executeUnderPathOrExit(file, "stash");
      GitUtil.executeUnderPathOrExit(file, "checkout", "-b", "sketch_branch", "origin/master");
      GitUtil.executeUnderPathOrExit(file, "checkout", "master", file.toAbsolutePath().toString());
    } catch (IOException | InterruptedException e) {
      GitClient.LOGGER.log(Level.SEVERE, "Exception occurred while initializing the git client: %s", e.toString());
    }
  }

  @Override
  public TaskExecutor executor() {
    return this;
  }
}
