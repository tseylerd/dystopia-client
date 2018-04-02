package com.dystopia.executor;

import com.dystopia.git.CommandLineExecutor;
import com.dystopia.git.GitUtil;

import java.io.IOException;
import java.nio.file.Path;

public class PullExecutor extends TaskExecutor {
    public PullExecutor(Path file) {
        super(file);
    }

    @Override
    public void run() {
        GitUtil.ensureUnderGit(file);
        try {
            CommandLineExecutor.execute(file.getParent().toFile(), "git", "fetch");
            CommandLineExecutor.execute(file.getParent().toFile(), "git", "checkout", "origin/master",
                    file.toAbsolutePath().toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
