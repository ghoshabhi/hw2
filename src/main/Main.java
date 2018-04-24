package main;

import task.Task;
import task.TaskFactory;
import taskpredicates.TaskPredicates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static taskpredicates.TaskPredicates.query;
import static taskpredicates.TaskPredicates.findByProjectName;
import static taskpredicates.TaskPredicates.findUnassignedTasks;

public class Main {
    public static void main(String[] args) {
        // Generate a task list
        List<Task> taskList = TaskFactory.getTaskList();
        // Helper date time objects to test predicates
        LocalDateTime dt1 = LocalDateTime.of(2018,3,18,22,22, 59);
        LocalDateTime dt2 = LocalDateTime.of(2018,12,18,8,22, 59);
        LocalDateTime dt3 = LocalDateTime.of(2018,4,24,00,00, 00);

        /**
         * Create a list of Predicate filters to apply on a task list to simulate a "query"
         */
        List<Predicate<Task>> filters = new ArrayList<>();

        filters.add(findByProjectName("Project_10"));
        filters.add(findUnassignedTasks());
        filters.add(TaskPredicates.findByTaskType(Task.TASK_TYPES.BACKEND));
        // The predicates above simulate the following query =>
        // "select * from Project where project.name == "ProjectName_10"
        // AND project.user == null
        // AND project.type == BACKEND;"

        List<Task> filteredTaskList = query(taskList, filters);
        System.out.println("Filtered Items: \n");
        filteredTaskList.forEach(System.out::println);
        System.out.println("--------------------------------");

        System.out.println("Use .map() to only get a task's description from the filtered list above: \n");
        List<String> taskDescriptions = filteredTaskList
                .stream()
                .map(Task::getTaskDescription)
                .collect(Collectors.toList());
        taskDescriptions.forEach(System.out::println);
        System.out.println("--------------------------------");

        /**
         * Custom predicates usage
         */
        List<Predicate<Task>> customPredicates = new ArrayList<>();

        Predicate<Task> notNull = Objects::nonNull;
        Predicate<Task> getByDate24thApril= task -> task.getTaskDeadline().equals(dt3);
        customPredicates.addAll(Arrays.asList(notNull, getByDate24thApril));

        List<Task> filteredTaskList2 = query(taskList, customPredicates);
        System.out.println("Filtered Task List with Custom Predicates: \n");
        filteredTaskList2.forEach(System.out::println);
        System.out.println("--------------------------------");


        /**
         * The following is the example usage of TaskPredicates.getFilteredTaskList function which
         * takes a List and a single predicate to apply to the list.
         */

        System.out.println("\n");
        System.out.println("Unassigned Tasks: ");
        TaskPredicates.getFilteredTaskList(taskList, TaskPredicates.findUnassignedTasks()).forEach(System.out::println);

        System.out.println("\n");
        System.out.println("Find By Project Name: ");
        TaskPredicates.getFilteredTaskList(taskList, TaskPredicates.findByProjectName("Project_1")).forEach(System.out::println);

        System.out.println("\n");
        System.out.println("Filter By Task Type (BACKEND): ");
        TaskPredicates.getFilteredTaskList(taskList, TaskPredicates.findByTaskType(Task.TASK_TYPES.BACKEND)).forEach(System.out::println);

        System.out.println("\n");
        System.out.println("Find By Deadline range: ");
        TaskPredicates.getFilteredTaskList(taskList, TaskPredicates.findByDeadline(dt1, dt2)).forEach(System.out::println);
    }
}

