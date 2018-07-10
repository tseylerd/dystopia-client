package com.dystopia.git;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public final class GitUtil {
    private GitUtil() {
    }

    public static boolean isUnderGit(Path file) {
        return Files.isDirectory(file.resolveSibling(".git"));
    }

    public static void ensureUnderGit(Path file) {
        if (!isUnderGit(file)) {
            throw new IllegalStateException("No git repository here.");
        }
    }

    public static int execute(Path directory, String... command) throws IOException, InterruptedException {
        String[] gitCommand = new String[command.length + 1];
        System.arraycopy(command, 0, gitCommand, 1, command.length);
        gitCommand[0] = "git";
        ProcessBuilder builder = new ProcessBuilder(gitCommand);
        Map<String, String> environment = builder.environment();
        URL location = GitUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String path = location.getPath();
        Path myPath = Paths.get(path);

        Path driverRunnerPath = myPath.resolveSibling("run-driver.sh");
        environment.put("DRIVER_PATH", driverRunnerPath.toString());
        return builder.
                directory(directory.toFile()).
                inheritIO().start().waitFor();
    }

    public static void executeUnderPathOrExit(Path file, String... command) throws IOException, InterruptedException {
        int code =  execute(file.getParent(), command);
        if (code != 0) {
            System.exit(code);
        }
    }
}
