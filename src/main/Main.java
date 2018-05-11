/*
 * @author Abhishek Ghosh
 */
package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import task.Task;
import task.TaskFactory;
import taskpredicates.TaskPredicates;
import ui.TasksTableView;

import java.util.Arrays;
import java.util.List;

import static taskpredicates.TaskPredicates.query;
import static taskpredicates.TaskPredicates.findByProjectName;
import static taskpredicates.TaskPredicates.findUnassignedTasks;
import static taskpredicates.TaskPredicates.findByTaskType;

public class Main extends Application {
    Stage window;
    Scene scene;

    TableView<Task> taskTableView;
    ComboBox<String> comboBoxFilter;
    TextField tfSearchQuery, tfFrom, tfTo;
    Button btnSearch, btnClear;
    HBox searchBoxView;

    ObservableList<Task> tasks;
    ObservableList<Task> filteredTasks;

    private final String[] SINGLE_TEXT_FIELD_FILTERS = {"Project Name", "Task Name", "User ID"};
    private boolean SINGLE_FILTER_ACTIVE = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Project Management Application");

        comboBoxFilter = new ComboBox<>();
        comboBoxFilter.setPromptText("Choose a Filter...");
        comboBoxFilter.getItems().addAll("Task Name", "Project Name", "Completion Date", "Deadline");
        comboBoxFilter
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((v, oldValue, newValue) -> handleChoiceBoxFilterChange(newValue));

        searchBoxView = new HBox(10);
        searchBoxView.setPadding(new Insets(10, 10, 10, 10));
        searchBoxView.getChildren().addAll(comboBoxFilter);


        taskTableView = TasksTableView.getTableView();
        taskTableView.setItems(getTasks());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(taskTableView, searchBoxView);

        scene = new Scene(layout, 690, 400);
        window.setScene(scene);
        window.show();
    }

    public ObservableList<Task> getTasks() {
        tasks = FXCollections.observableArrayList(TaskFactory.getTaskList());
        return tasks;
    }

    public void handleChoiceBoxFilterChange(String filter) {
        if (Arrays.asList(SINGLE_TEXT_FIELD_FILTERS).contains(filter)) {
            if (!SINGLE_FILTER_ACTIVE) {
                SINGLE_FILTER_ACTIVE = true;
                // Cleanup
                searchBoxView.getChildren().removeAll(tfFrom, tfTo, btnSearch, btnClear);

                // set new fields
                tfSearchQuery = new TextField();
                tfSearchQuery.setPromptText(filter);
                tfSearchQuery.setMinWidth(100);

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                searchBoxView.getChildren().addAll(tfSearchQuery, btnSearch, btnClear);
            } else {
                tfSearchQuery.setPromptText(filter);
            }
        } else {
            SINGLE_FILTER_ACTIVE = false;
            // Cleanup
            searchBoxView.getChildren().removeAll(tfSearchQuery, btnSearch, btnClear);
            tfFrom = new TextField();
            tfFrom.setPromptText("From");
            tfFrom.setMinWidth(100);

            // set new fields
            tfTo = new TextField();
            tfTo.setPromptText("To");
            tfTo.setMinWidth(100);

            // Search and Clear buttons
            btnSearch = new Button("Search");
            btnSearch.setOnAction(e -> handleSearch());
            btnClear = new Button("Clear");
            btnClear.setOnAction(e -> handleClear());

            searchBoxView.getChildren().addAll(tfTo, tfFrom, btnSearch, btnClear);
        }
    }

    public void handleSearch() {
        String taskName = tfSearchQuery.getText();
        List<Task> filtered = TaskPredicates.getFilteredTaskList((List) tasks, findByProjectName(taskName));
        filteredTasks = FXCollections.observableArrayList(filtered);
        taskTableView.getItems().removeAll();
        taskTableView.setItems(filteredTasks);
    }

    public void handleClear() {
        filteredTasks = null;
        taskTableView.getItems().removeAll();
        taskTableView.setItems(getTasks());
        tfSearchQuery.clear();
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

