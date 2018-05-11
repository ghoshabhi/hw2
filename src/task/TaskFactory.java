/*
 * @author Abhishek Ghosh
 */
package task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/* TaskFactory class to generate a bunch of tasks to work with */
public class TaskFactory {
    public static List<Task> getTaskList() {
        LocalDateTime dt1 = LocalDateTime.of(2018, 3, 18, 22, 22, 59);
        LocalDateTime dt2 = LocalDateTime.of(2018, 12, 18, 8, 22, 59);
        LocalDateTime dt3 = LocalDateTime.of(2018, 6, 18, 8, 22, 59);
        LocalDateTime dt4 = LocalDateTime.of(2018, 4, 24, 00, 00, 00);
        LocalDateTime dt5 = LocalDateTime.of(2017, 10, 18, 8, 22, 59);
        LocalDateTime dt6 = LocalDateTime.of(2018, 3, 18, 8, 22, 59);

        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task(
                    "Project_" + (i + 1),
                    "Task_" + (i + 1),
                    i + 1, // taskId
                    TaskType.getRandomTaskType(),
                    "some description_" + (i + 1),
                    "userId_" + (i + 1),
                    dt1
            );
            taskList.add(task);
            dt1 = dt1.plusMonths(1).plusDays(5);
        }
        taskList.add(new Task(
                        "Project_" + (taskList.size() + 1),
                        "Task_" + (taskList.size() + 1),
                        taskList.size() + 1,
                        TaskType.getRandomTaskType(),
                        "some description_" + (taskList.size() + 1),
                        "userId_" + (taskList.size() + 1),
                        dt3.plusMonths(1)
                )
        );
        taskList.add(new Task(
                        "Project_" + (taskList.size() + 1),
                        "Task_" + (taskList.size() + 1),
                        taskList.size() + 1,
                        TaskType.BACKEND,
                        "some description_" + (taskList.size() + 1),
                        null,
                        dt1
                )
        );
        taskList.add(new Task(
                        "Project_" + (taskList.size() + 1),
                        "Task_" + (taskList.size() + 1),
                        taskList.size() + 1,
                        TaskType.BACKEND,
                        "some description_" + (taskList.size() + 1),
                        null,
                        dt1
                )
        );
        taskList.add(new Task(
                        "Project_" + (taskList.size() + 1),
                        "Task_" + (taskList.size() + 1),
                        taskList.size() + 1,
                        TaskType.BACKEND,
                        "some description_" + (taskList.size() + 1),
                        null,
                        dt4
                )
        );
        // to use to test filterByCompletionDate predicate
        taskList.add(new Task(
                        "Project_" + (taskList.size() + 1),
                        "Task_" + (taskList.size() + 1),
                        taskList.size() + 1,
                        TaskType.FRONTEND,
                        "some description_" + (taskList.size() + 1),
                        null,
                        dt5
                )
        );
        taskList.add(new Task(
                        "Project_" + (taskList.size() + 1),
                        "Task_" + (taskList.size() + 1),
                        taskList.size() + 1,
                        TaskType.QA,
                        "some description_" + (taskList.size() + 1),
                        null,
                        dt5
                )
        );
        return taskList;
    }
}
