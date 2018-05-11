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
import task.TaskType;
import ui.TasksTableView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static taskpredicates.TaskPredicates.*;

public class Main extends Application {
    Stage window;
    Scene scene;

    TableView<Task> taskTableView;
    ComboBox<String> comboBoxFilter, comboBoxTaskType;
    TextField tfSearchQuery, tfFrom, tfTo;
    DatePicker dtFrom, dtTo;
    Button btnSearch, btnClear;

    VBox searchBoxView;
    HBox singleFilter;
    HBox multiFilter;

    ObservableList<Task> tasks;
    ObservableList<Task> filteredTasks;

    private final String[] SINGLE_TEXT_FIELD_FILTERS = {"Project Name", "User ID"};
    private boolean SINGLE_FILTER_ACTIVE, RANGE_FILTER_ACTIVE, DROPDOWN_FILTER_ACTIVE = false;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Project Management Application");

        comboBoxFilter = new ComboBox<>();
        comboBoxFilter.setPromptText("Choose a Filter...");
        comboBoxFilter.getItems().addAll("Project Name", "Task Type", "User ID", "Completion Date", "Deadline");
        comboBoxFilter
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((v, oldValue, newValue) -> handleChoiceBoxFilterChange(newValue));

        searchBoxView = new VBox(10);
        searchBoxView.setPadding(new Insets(10, 10, 10, 10));
        Separator separator = new Separator();

        singleFilter = new HBox(10);
        multiFilter = new HBox(10);

        singleFilter.getChildren().add(comboBoxFilter);
        Button btn = new Button("Demo");
        multiFilter.getChildren().add(btn);

        searchBoxView.getChildren().addAll(singleFilter, separator, multiFilter);


        taskTableView = TasksTableView.getTableView();
        taskTableView.setItems(getTasks());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(taskTableView, searchBoxView);

        scene = new Scene(layout, 800, 450);
        window.setScene(scene);
        window.show();
    }

    private ObservableList<Task> getTasks() {
        List<Task> list = TaskFactory.getTaskList();
        tasks = FXCollections.observableArrayList(list);
        return tasks;
    }

    private void handleChoiceBoxFilterChange(String filter) {
        if (Arrays.asList(SINGLE_TEXT_FIELD_FILTERS).contains(filter)) {
            if (!SINGLE_FILTER_ACTIVE) {
                SINGLE_FILTER_ACTIVE = true;
                RANGE_FILTER_ACTIVE = false;
                DROPDOWN_FILTER_ACTIVE = false;
                // Cleanup
                singleFilter.getChildren().removeAll(dtFrom, dtTo, comboBoxTaskType, btnSearch, btnClear);

                // set new fields
                tfSearchQuery = new TextField();
                tfSearchQuery.setPromptText("Enter " + filter);
                tfSearchQuery.setMinWidth(100);

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                singleFilter.getChildren().addAll(tfSearchQuery, btnSearch, btnClear);
            } else {
                tfSearchQuery.setPromptText("Enter " + filter);
            }
        } else if(filter == "Task Type") {
            if(!DROPDOWN_FILTER_ACTIVE) {
                DROPDOWN_FILTER_ACTIVE = true;
                RANGE_FILTER_ACTIVE = false;
                SINGLE_FILTER_ACTIVE = false;

                // Cleanup
                singleFilter.getChildren().removeAll(dtFrom, dtTo, tfSearchQuery, btnSearch, btnClear);

                comboBoxTaskType = new ComboBox<>();
                comboBoxTaskType.setPromptText("Choose a Filter...");
                comboBoxTaskType.getItems().addAll("FrontEnd", "BackEnd", "QA");

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                singleFilter.getChildren().addAll(comboBoxTaskType, btnSearch, btnClear);
            }
        } else {
            if(!RANGE_FILTER_ACTIVE ) {
                RANGE_FILTER_ACTIVE = true;
                SINGLE_FILTER_ACTIVE = false;
                DROPDOWN_FILTER_ACTIVE = false;
                // Cleanup
                singleFilter.getChildren().removeAll(tfSearchQuery, comboBoxTaskType, btnSearch, btnClear);

                dtFrom = new DatePicker();
                dtFrom.setPromptText("From Date");
                dtTo = new DatePicker();
                dtTo.setPromptText("To Date");

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                singleFilter.getChildren().addAll(dtFrom, dtTo, btnSearch, btnClear);
            }
        }
    }

    private void handleSearch() {
        if(SINGLE_FILTER_ACTIVE) {
            String query = tfSearchQuery.getText();
            String filterName = comboBoxFilter.getValue();
            List<Task> filtered = null;
            //"Project Name", "Task Type", "User ID"
            switch(filterName) {
                case "Project Name": {
                    filtered = getFilteredTaskList((List)tasks, findByProjectName(query));
                    break;
                }
                case "User ID": {
                    filtered = getFilteredTaskList((List)tasks, findByUserId(query));
                    break;
                }
                default: {
                    filtered = (List)tasks;
                    break;
                }
            }
            filteredTasks = FXCollections.observableArrayList(filtered);
            taskTableView.getItems().removeAll();
            taskTableView.setItems(filteredTasks);
        } else if (RANGE_FILTER_ACTIVE) {
            LocalDateTime ldtFrom = formatDate(dtFrom.getValue().toString());
            LocalDateTime ldtTo = formatDate(dtTo.getValue().toString());
            String filterName = comboBoxFilter.getValue();

            List<Task> filtered = null;

            if(filterName == "Deadline") {
                filtered = getFilteredTaskList((List)tasks, findByDeadline(ldtFrom, ldtTo));
            } else {
                filtered = getFilteredTaskList((List)tasks, findByCompletionDate(ldtFrom, ldtTo));
            }

            filteredTasks = FXCollections.observableArrayList(filtered);
            taskTableView.getItems().removeAll();
            taskTableView.setItems(filteredTasks);
        } else if (DROPDOWN_FILTER_ACTIVE){
            String strTaskType = comboBoxTaskType.getValue();
            TaskType taskType = TaskType.valueOf(strTaskType.toUpperCase());

            List<Task> filtered = getFilteredTaskList((List)tasks, findByTaskType(taskType));

            filteredTasks = FXCollections.observableArrayList(filtered);
            taskTableView.getItems().removeAll();
            taskTableView.setItems(filteredTasks);
        }
    }

    private LocalDateTime formatDate(String dt) {
        LocalDate localDate = LocalDate.parse(dt, DATE_FORMATTER);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalDateTime.now().toLocalTime());
        return localDateTime;
    }

    private void handleClear() {
        filteredTasks = null;
        if(DROPDOWN_FILTER_ACTIVE) {
            comboBoxTaskType.getSelectionModel().clearSelection();
        } else if(RANGE_FILTER_ACTIVE) {
            dtFrom.getEditor().clear();
            dtTo.getEditor().clear();
        } else {
            tfSearchQuery.clear();
        }

        taskTableView.getItems().removeAll();
        taskTableView.setItems(tasks);
    }
}

