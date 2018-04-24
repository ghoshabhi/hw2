package taskpredicates;

import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskPredicates {
    /**
     * Applys a List<Predicate<Task>> to the provided task list
     * @param taskList a list of tasks to apply the filters
     * @param filters a list of filters to apply to the task list
     * @return filtered list
     */
    public static List<Task> query(List<Task> taskList, List<Predicate<Task>> filters) {
        Predicate<Task> allFilters = filters.stream().reduce(Predicate::and).orElse(x -> true);

        return taskList.stream().filter(allFilters).collect(Collectors.toList());
    }

    /**
     * Applys a single Predicate to the provided task list
     * @param taskList a list of tasks to apply the filters
     * @param predicate a predicate function to apply to the task list
     * @return filtered list
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
     * Filters the task list to return all projects matching the given projectName
     * @param projectName name of the project to search
     * @return filtered list
     */
    public static Predicate<Task> findByProjectName (String projectName) {
        return task -> task.getProjectName().equals(projectName);
    }

    /**
     * Filters the task list to return all projects matching the given userId
     * @param userId userId of the assigned user
     * @return filtered list
     */
    public static Predicate<Task> findByUserId (String userId) {
        return task -> task.getUserId().equals(userId);
    }

    /**
     * Filters the task list to return all projects matching the given taskType
     * @param taskType one of the Task.TASK_TYPES
     * @return filtered list
     */
    public static Predicate<Task> findByTaskType (Task.TASK_TYPES taskType) {
        return task -> task.getTaskType() == taskType;
    }

    /**
     * Filters the task list to return all projects between the given start and end data of deadline.
     * NOTE: This doesn't include the start and end date itself.
     * @param startDate starting range
     * @param endDate   end range
     * @return filtered list
     */
    public static Predicate<Task> findByDeadline(LocalDateTime startDate, LocalDateTime endDate) {
        return task -> task.getTaskDeadline().isAfter(startDate) && task.getTaskDeadline().isBefore(endDate);
    }

    /**
     * Filters the task list to return all projects between the given start and end data of completion date.
     * NOTE: This doesn't include the start and end date itself.
     * @param startDate starting range
     * @param endDate   end range
     * @return filtered list
     */
    public static Predicate<Task> findByCompletionDate(LocalDateTime startDate, LocalDateTime endDate) {
        return task -> task.getTaskCompletedOn().isAfter(startDate) && task.getTaskCompletedOn().isBefore(endDate);
    }

    /**
     * Returns all projects which don't have a user assigned to them
     * @return filtered list
     */
    public static Predicate<Task> findUnassignedTasks() {
        return task -> task.getUserId() == null;
    }

}
