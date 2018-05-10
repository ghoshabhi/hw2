/*
 * @author Abhishek Ghosh
 */
package main;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import task.Task;
import task.TaskFactory;
import taskpredicates.TaskPredicates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static taskpredicates.TaskPredicates.query;
import static taskpredicates.TaskPredicates.findByProjectName;
import static taskpredicates.TaskPredicates.findUnassignedTasks;
import static taskpredicates.TaskPredicates.findByTaskType;

public class Main extends Application {
    Stage window;
    Scene scene;
    TableView<Task> taskTableView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Project Management Application");

        TableColumn<Task, Integer> colTaskId = new TableColumn<>("TaskId");
        colTaskId.setMinWidth(50);
        colTaskId.setStyle("-fx-alignment: CENTER");
        colTaskId.setCellValueFactory(new PropertyValueFactory<>("taskId"));

        TableColumn<Task, String> colTaskName = new TableColumn<>("Task Name");
        colTaskName.setMinWidth(250);
        colTaskName.setStyle("-fx-alignment: CENTER");
        colTaskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<Task, String> colProjectName = new TableColumn<>("Project Name");
        colProjectName.setMinWidth(250);
        colProjectName.setStyle("-fx-alignment: CENTER");
        colProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));

        TableColumn<Task, String> colTaskType = new TableColumn<>("Task Type");
        colTaskType.setMinWidth(250);
        colTaskType.setStyle("-fx-alignment: CENTER");
        colTaskType.setCellValueFactory(new PropertyValueFactory<>("taskType"));

        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
        TableColumn<Task, LocalDateTime> colTaskDeadline = new TableColumn<>("Deadline");
        colTaskDeadline.setMinWidth(250);
        colTaskDeadline.setStyle("-fx-alignment: CENTER");
        colTaskDeadline.setCellValueFactory(new PropertyValueFactory<>("taskDeadline"));
        colTaskDeadline.setCellFactory(column -> {
            return new TableCell<Task, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        // Format date.
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });


        taskTableView = new TableView<>();
        taskTableView.setItems(getTasks());
        taskTableView.getColumns().addAll(colTaskId, colTaskName, colProjectName, colTaskType, colTaskDeadline);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(taskTableView);

        scene = new Scene(layout, 1200,550);
        window.setScene(scene);
        window.show();
    }

    public ObservableList<Task> getTasks() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        tasks = TaskFactory.getTaskList();
        return tasks;
    }

    //        /* Generate a task list to work with. Please check out the "TaskFactory" class in the "task" package to
//        * generate any additional task objects to test out the predicate logic.
//        */
//        List<Task> taskList = TaskFactory.getTaskList();
//        // Helper date time objects to test the predicates
//        LocalDateTime dt1 = LocalDateTime.of(2018,3,18,22,22, 59);
//        LocalDateTime dt2 = LocalDateTime.of(2018,12,18,8,22, 59);
//        LocalDateTime dt3 = LocalDateTime.of(2018,4,24,00,00, 00);
//
//        /**
//         * Create a list of Predicate filters to apply on a task list to simulate a "query"
//         */
//        List<Predicate<Task>> filters = new ArrayList<>();
//
//        filters.add(findByProjectName("Project_10"));
//        filters.add(findUnassignedTasks());
//        filters.add(findByTaskType(Task.TASK_TYPES.BACKEND));
//        /*
//         * The predicates above simulate the following query =>
//         * "select * from Project where project.name == "ProjectName_10"
//         * AND project.user == null
//         * AND project.type == BACKEND;"
//         */
//
//        System.out.println("Filtered Items: \n");
//
//        List<Task> filteredTaskList = query(taskList, filters);
//        filteredTaskList.forEach(System.out::println);
//
//        System.out.println("--------------------------------");
//
//        System.out.println("Using .map() to only get a task's description from the filtered list above: \n");
//
//        List<String> taskDescriptions = filteredTaskList
//                .stream()
//                .map(Task::getTaskDescription)
//                .collect(Collectors.toList());
//        taskDescriptions.forEach(System.out::println);
//
//        System.out.println("--------------------------------");
//
//        /**
//         * Example of building custom predicates and putting them in a list to use with the "TaskPredicates.query" function.
//         */
//        List<Predicate<Task>> customPredicates = new ArrayList<>();
//
//        Predicate<Task> notNull = Objects::nonNull;
//        Predicate<Task> getByDate24thApril= task -> task.getTaskDeadline().equals(dt3);
//
//        customPredicates.addAll(Arrays.asList(notNull, getByDate24thApril));
//
//        List<Task> filteredTaskList2 = query(taskList, customPredicates);
//        System.out.println("Filtered Task List with Custom Predicates: \n");
//        filteredTaskList2.forEach(System.out::println);
//        System.out.println("--------------------------------");
//
//
//        /**
//         * The following is the example usage of "TaskPredicates.getFilteredTaskList" function which
//         * takes a list of tasks and a single predicate function to apply to the list.
//         */
//        System.out.println("\n");
//        System.out.println("Unassigned Tasks: ");
//        TaskPredicates
//                .getFilteredTaskList(taskList, TaskPredicates.findUnassignedTasks())
//                .forEach(System.out::println);
//
//        System.out.println("\n");
//        System.out.println("Find By Project Name: ");
//        TaskPredicates
//                .getFilteredTaskList(taskList, TaskPredicates.findByProjectName("Project_1"))
//                .forEach(System.out::println);
//
//        System.out.println("\n");
//        System.out.println("Filter By Task Type (BACKEND): ");
//        TaskPredicates
//                .getFilteredTaskList(taskList, TaskPredicates.findByTaskType(Task.TASK_TYPES.BACKEND))
//                .forEach(System.out::println);
//
//        System.out.println("\n");
//        System.out.println("Find By Deadline range: ");
//        TaskPredicates
//                .getFilteredTaskList(taskList, TaskPredicates.findByDeadline(dt1, dt2))
//                .forEach(System.out::println);
}

