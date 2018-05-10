/*
 * @author Abhishek Ghosh
 */
package task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/* TaskFactory class to generate a bunch of tasks to work with */
public class TaskFactory {
    public static ObservableList<Task> getTaskList() {
        LocalDateTime dt1 = LocalDateTime.of(2018,3,18,22,22, 59);
        LocalDateTime dt2 = LocalDateTime.of(2018,12,18,8,22, 59);
        LocalDateTime dt3 = LocalDateTime.of(2018,6,18,8,22, 59);
        LocalDateTime dt4 = LocalDateTime.of(2018,4,24,00,00, 00);
        LocalDateTime dt5 = LocalDateTime.of(2017,10,18,8,22, 59);
        LocalDateTime dt6 = LocalDateTime.of(2018,3,18,8,22, 59);

        ObservableList<Task> taskList = FXCollections.observableArrayList();
        for(int i=0; i < 10; i++) {
            dt1 = dt1.plusMonths(1);
            Task task = new Task(
                "Project_" + (i + 1),
                "Task_" + (i + 1),
                    i + 1, // taskId
                Task.TASK_TYPES.getRandomTaskType(),
                "some description_" + (i + 1),
                "userId_" + (i + 1),
                dt1
            );
            taskList.add(task);
        }
        taskList.add(new Task(
                "Project_" + (taskList.size() + 1),
                "Task_" + (taskList.size() + 1),
                taskList.size() + 1,
                Task.TASK_TYPES.getRandomTaskType(),
                "some description_" + (taskList.size() + 1),
                "userId_" + (taskList.size() + 1),
                dt3.plusDays(1)
            )
        );
//        taskList.add(new Task(
//                    "Project_" + 10,
//                    "Task_"+11,
//                    11,
//                    Task.TASK_TYPES.BACKEND,
//                    "some description_"+11,
//                    null,
//                    dt1
//            )
//        );
//        taskList.add(new Task(
//                        "Project_" + 10,
//                        "Task_"+12,
//                        12,
//                        Task.TASK_TYPES.BACKEND,
//                        "some description_"+12,
//                        null,
//                        dt1
//                )
//        );
//        taskList.add(new Task(
//                        "Project_" + 10,
//                        "Task_"+13,
//                        13,
//                        Task.TASK_TYPES.BACKEND,
//                        "some description_"+13,
//                        null,
//                        dt4
//                )
//        );
//        // to use to test filterByCompletionDate predicate
//        taskList.add(new Task(
//                        "Project_" + 2000,
//                        "Task_"+14,
//                        14,
//                        Task.TASK_TYPES.FRONTEND,
//                        "some description_"+13,
//                        null,
//                        dt5
//                )
//        );
//        taskList.add(new Task(
//                        "Project_" + 2000,
//                        "Task_"+15,
//                        15,
//                        Task.TASK_TYPES.QA,
//                        "some description_"+13,
//                        null,
//                        dt5
//                )
//        );
        return taskList;
    }
}
