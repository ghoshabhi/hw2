package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskFactory {
    public static List<Task> getTaskList() {
        LocalDateTime dt1 = LocalDateTime.of(2018,3,18,22,22, 59);
        LocalDateTime dt2 = LocalDateTime.of(2018,12,18,8,22, 59);
        LocalDateTime dt3 = LocalDateTime.of(2018,6,18,8,22, 59);
        LocalDateTime dt4 = LocalDateTime.of(2018,4,24,00,00, 00);
        LocalDateTime dt5 = LocalDateTime.of(2017,10,18,8,22, 59);
        LocalDateTime dt6 = LocalDateTime.of(2018,3,18,8,22, 59);

        List<Task> taskList = new ArrayList<>();
        for(int i=0; i< 5; i++) {
            Task task = new Task(
                "Project_" + i,
                "Task_"+i,
                i,
                Task.TASK_TYPES.getRandomTaskType(),
                "some description_"+i,
                "userId_"+i,
                dt2
            );
            taskList.add(task);
        }
        taskList.add(new Task(
                "Project_" + 10,
                "Task_"+10,
                10,
                Task.TASK_TYPES.getRandomTaskType(),
                "some description_"+10,
                "user_foo_1",
                dt3
            )
        );
        taskList.add(new Task(
                    "Project_" + 10,
                    "Task_"+11,
                    11,
                    Task.TASK_TYPES.BACKEND,
                    "some description_"+11,
                    null,
                    dt1
            )
        );
        taskList.add(new Task(
                        "Project_" + 10,
                        "Task_"+12,
                        12,
                        Task.TASK_TYPES.BACKEND,
                        "some description_"+12,
                        null,
                        dt1
                )
        );
        taskList.add(new Task(
                        "Project_" + 10,
                        "Task_"+13,
                        13,
                        Task.TASK_TYPES.BACKEND,
                        "some description_"+13,
                        null,
                        dt4
                )
        );
        // to use to test filterByCompletionDate predicate
        taskList.add(new Task(
                        "Project_" + 2000,
                        "Task_"+14,
                        13,
                        Task.TASK_TYPES.FRONTEND,
                        "some description_"+13,
                        null,
                        dt5
                )
        );
        taskList.add(new Task(
                        "Project_" + 2000,
                        "Task_"+15,
                        13,
                        Task.TASK_TYPES.QA,
                        "some description_"+13,
                        null,
                        dt5
                )
        );
        return taskList;
    }
}
