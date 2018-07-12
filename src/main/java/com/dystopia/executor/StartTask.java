package com.dystopia.executor;

import com.dystopia.Util;
import com.dystopia.definition.TaskDefinition;
import com.dystopia.git.GitUtil;
import com.dystopia.server.GitClient;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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
      Path gitDirectory = file.resolveSibling(".git");
      copyAndModify(gitDirectory, "config", path -> {
        Path currentJarPath = Util.getContainingPath();
        Path driverRunnerPath = currentJarPath.resolveSibling("run-driver.sh");
        GitClient.LOGGER.severe(driverRunnerPath.toString());
        GitClient.LOGGER.severe(new File(driverRunnerPath.toUri()).getAbsolutePath());
        Files.write(
          path,
          Arrays.asList(
            "",
            "[merge \"dystopia\"]",
            "\tname = dystopia",
            "\tdriver = /bin/sh '" + URLDecoder.decode(driverRunnerPath.toFile().getPath(), "UTF-8") + "' %O %A %B"
          ),
          StandardOpenOption.APPEND,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE
        );
      });

      copyAndModify(file.getParent(), ".gitattributes", path -> Files.write(
        path,
        Collections.singletonList("*.sketch merge=dystopia"),
        StandardOpenOption.APPEND,
        StandardOpenOption.CREATE,
        StandardOpenOption.WRITE
      ));

      GitUtil.execute(file.getParent(), "add", file.toAbsolutePath().toString());
    } catch (IOException | InterruptedException e) {
      GitClient.LOGGER.log(Level.SEVERE, e, e::getMessage);
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
        Path copy = file.resolveSibling(String.format("%s_copy", fileName));
        Files.copy(file, copy, StandardCopyOption.REPLACE_EXISTING);
      }
      operation.perform(file);
    }
  }

  private interface FileOperation {
    void perform(Path path) throws IOException;
  }
}
