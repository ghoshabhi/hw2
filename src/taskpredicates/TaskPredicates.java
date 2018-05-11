/*
 * @author Abhishek Ghosh
 */
package taskpredicates;

import task.Task;
import task.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskPredicates {
    /**
     * Applys a list of filters to the provided task list. Each filter
     * is a Predicate function. The following function merges each of the filter in the list of
     * filters using the "reduce" function and combines into one predicate function.
     * It then applies this function to the provided list of tasks and returns a filtered task list.
     * Since the filters are combined, the list is only traversed once to get the output.
     * @param taskList a list of tasks
     * @param filters a list of filters to apply to the task list
     * @return A filtered task list
     */
    public static List<Task> query(List<Task> taskList, List<Predicate<Task>> filters) {
        Predicate<Task> allFilters = filters.stream().reduce(Predicate::and).orElse(x -> true);
        return taskList.stream().filter(allFilters).collect(Collectors.toList());
    }

    /**
     * Applys a single Predicate function to the provided task list. It scans through
     * the provided task list once and applies the predicate function to each Task
     * object and if it satisfies the predicate test, it gets added to the "filtered"
     * list of tasks.
     * @param taskList list of tasks
     * @param predicate predicate function to apply to the task list
     * @return filtered task list
     */
    public static List<Task> getFilteredTaskList(List<Task> taskList, Predicate<Task> predicate) {
        List<Task> filtered = new ArrayList<>();
        for(Task task : taskList) {
            if(predicate.test(task)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    /**
     * Filters the task list to return all projects matching the given "projectName".
     * This function creates and returns a Predicate which can then be applied to a
     * list of tasks.
     * @param projectName name of the project to search
     * @return filtered list
     */
    public static Predicate<Task> findByProjectName (String projectName) {
        return task -> task.getProjectName().equals(projectName);
    }

    /**
     * Filters the task list to return all projects matching the given "userId".
     * This function creates and returns a Predicate which can then be applied to a
     * list of tasks.
     * @param userId The id of the user to find the list of tasks by
     * @return Filtered list
     */
    public static Predicate<Task> findByUserId (String userId) {
        return task -> task.getUserId().equals(userId);
    }

    /**
     * Filters the task list to return all projects matching the given "taskType".
     * This function creates and returns a Predicate which can then be applied to a
     * list of tasks.
     * @param taskType One of the Task.TASK_TYPES. Check out the "Task" class to see
     *                 valid options.
     * @return Filtered list
     */
    public static Predicate<Task> findByTaskType (TaskType taskType) {
        return task -> task.getTaskType() == taskType;
    }

    /**
     * Filters the task list to return all projects whose "deadline" falls between the "startDate"
     *  and "endDate". This function creates and returns a Predicate which can then be applied to a
     * list of tasks.
     * NOTE: This doesn't include the "startDate" and "endDate" itself. Think of this as open
     *       interval as in Mathematics. Eg: (2,3) doesn't include 2 and 3 itself.
     * @param startDate starting date for the range
     * @param endDate   ending date for the range
     * @return Filtered list
     */
    public static Predicate<Task> findByDeadline(LocalDateTime startDate, LocalDateTime endDate) {
        return task -> task.getTaskDeadline().isAfter(startDate) && task.getTaskDeadline().isBefore(endDate);
    }

    /**
     * Filters the task list to return all projects whose "completion date" falls between the "startDate"
     * and "endDate". This function creates and returns a Predicate which can then be applied to a
     * list of tasks.
     * NOTE: This doesn't include the "startDate" and "endDate" itself. Think of this as open
     *       interval as in Mathematics. Eg: (2,3) doesn't include 2 and 3 itself.
     * NOTE: This doesn't include the start and end date itself.
     * @param startDate starting date for the range
     * @param endDate   ending date for the range
     * @return Filtered list
     */
    public static Predicate<Task> findByCompletionDate(LocalDateTime startDate, LocalDateTime endDate) {
        return task -> task.getTaskCompletedOn().isAfter(startDate) && task.getTaskCompletedOn().isBefore(endDate);
    }

    /**
     * Filters the task list to return all projects which don't have userId assigned to them. This
     * function creates and returns a Predicate which can then be applied to a list of tasks.
     * @return filtered list
     */
    public static Predicate<Task> findUnassignedTasks() {
        return task -> task.getUserId() == null;
    }

}
