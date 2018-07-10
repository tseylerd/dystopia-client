package com.dystopia.definition;

import com.dystopia.executor.TaskExecutor;

public interface TaskDefinition {
    TaskExecutor executor();
}
