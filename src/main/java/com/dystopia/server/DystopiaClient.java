package com.dystopia.server;

import com.dystopia.executor.*;

public class DystopiaClient {

    public static void main(String[] args) {
        try {
            TaskExecutor taskExecutor = ArgumentsParser.parse(args);
            taskExecutor.run();
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }
    }
}

