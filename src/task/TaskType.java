package task;

import java.util.Random;

public enum TaskType {
    FRONTEND,
    BACKEND,
    QA;

    public static TaskType getRandomTaskType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
};