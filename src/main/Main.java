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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static taskpredicates.TaskPredicates.*;

public class Main extends Application {
    ObservableList<Task> tasks;
    ObservableList<Task> filteredTasks;

    Stage window;
    Scene scene;

    TableView<Task> taskTableView;

    VBox searchBoxView;
    VBox singleFilterView;
    VBox multiFilterView;

    ComboBox<String> comboBoxFilter, comboBoxTaskType;
    TextField tfSearchQuery;
    DatePicker dtFrom, dtTo;
    Button btnSearch, btnClear;

    TextField genTfProjectName, genTfUserId;
    ComboBox<String> genComboBoxTaskType;
    DatePicker genDtDeadlineFrom, genDtDeadlineTo;
    Button genBtnSearch, genBtnClear;

    private final String[] SINGLE_TEXT_FIELD_FILTERS = {"Project Name", "User ID"};
    private final String[] AVAILABLE_FILTERS = {"Project Name", "User ID", "Task Type", "Completion Date", "Deadline"};
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

        comboBoxFilter.getItems().addAll(AVAILABLE_FILTERS);
        comboBoxFilter
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((v, oldValue, newValue) -> handleChoiceBoxFilterChange(newValue));

        searchBoxView = new VBox(10);
        searchBoxView.setPadding(new Insets(10, 10, 10, 10));
        Separator separator = new Separator();

        singleFilterView = new VBox(10);
        multiFilterView = new VBox(10);

        Label singleSearchLbl = new Label("Single Query Search");
        singleSearchLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: #005abc; -fx-font-weight: bold;");
        singleFilterView.getChildren().addAll(singleSearchLbl, comboBoxFilter);

        // General search view components setup
        HBox hbProjName = new HBox(10);
        Label lblProjName = new Label("Project Name:");
        genTfProjectName = new TextField();
        genTfProjectName.setMaxWidth(100);
        hbProjName.getChildren().addAll(lblProjName, genTfProjectName);

        HBox hbUserId = new HBox(10);
        Label lblUserId = new Label("UserId:");
        genTfUserId = new TextField();
        genTfUserId.setMaxWidth(100);
        hbUserId.getChildren().addAll(lblUserId, genTfUserId);

        HBox hbTaskType = new HBox(10);
        Label lblTaskType = new Label("Task Type:");
        genComboBoxTaskType = new ComboBox<>();
        genComboBoxTaskType.setPromptText("Choose a Filter...");
        genComboBoxTaskType.getItems().addAll("FrontEnd", "BackEnd", "QA");
        hbTaskType.getChildren().addAll(lblTaskType, genComboBoxTaskType);

        HBox hbDeadline = new HBox(10);
        Label lblDeadline = new Label("Deadline:");
        genDtDeadlineFrom = new DatePicker();
        genDtDeadlineFrom.setPromptText("From Date");
        genDtDeadlineTo = new DatePicker();
        genDtDeadlineTo.setPromptText("To Date");
        hbDeadline.getChildren().addAll(lblDeadline, genDtDeadlineFrom, genDtDeadlineTo);

        HBox hbGenSearchBtns = new HBox(10);
        genBtnSearch = new Button("Search");
        genBtnSearch.setStyle("-fx-background-color: #69b854; -fx-background-radius: 10; -fx-text-fill: #fff");
        genBtnSearch.setOnAction(e -> handleGenSearch());
        genBtnClear = new Button("Clear");
        genBtnClear.setStyle("-fx-background-color: #e84946; -fx-background-radius: 10; -fx-text-fill: #fff");
        genBtnClear.setOnAction(e -> handleGenClear());
        hbGenSearchBtns.getChildren().addAll(genBtnSearch, genBtnClear);

        Label multiSearchLbl = new Label("Multiple Query Search");
        multiSearchLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: #005abc; -fx-font-weight: bold;");

        multiFilterView.getChildren().addAll(multiSearchLbl, hbProjName, hbUserId, hbTaskType, hbDeadline, hbGenSearchBtns);

        // General search view components setup end

        searchBoxView.getChildren().addAll(singleFilterView, separator, multiFilterView);


        taskTableView = TasksTableView.getTableView();
        taskTableView.setItems(getTasks());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(taskTableView, searchBoxView);

        scene = new Scene(layout, 900, 650);
        window.setScene(scene);
        window.show();
    }

    private ObservableList<Task> getTasks() {
        List<Task> list = TaskFactory.getTaskList();
        tasks = FXCollections.observableArrayList(list);
        return tasks;
    }

    private void handleGenSearch() {
        List<Predicate<Task>> filters = new ArrayList<>();

        if (!genTfProjectName.getText().trim().isEmpty()) {
            filters.add(findByProjectName(genTfProjectName.getText()));
        }
        if (!genTfUserId.getText().isEmpty()) {
            filters.add(findByUserId(genTfUserId.getText()));
        }
        if (genDtDeadlineFrom.getValue() != null && genDtDeadlineTo.getValue() != null) {
            LocalDateTime ldtFrom = formatDate(genDtDeadlineFrom.getValue().toString());
            LocalDateTime ldtTo = formatDate(genDtDeadlineTo.getValue().toString());
            filters.add(findByDeadline(ldtFrom, ldtTo));
        }
        if (!genComboBoxTaskType.getSelectionModel().isEmpty()) {
            String strTaskType = genComboBoxTaskType.getValue();
            TaskType taskType = TaskType.valueOf(strTaskType.toUpperCase());
            filters.add(findByTaskType(taskType));
        }

        if (filters.size() != 0) {
            List<Task> filtered = query((List) tasks, filters);
            filteredTasks = FXCollections.observableArrayList(filtered);
            taskTableView.getItems().removeAll();
            taskTableView.setItems(filteredTasks);
        }
    }

    private void handleGenClear() {
        filteredTasks = null;

        genTfProjectName.clear();
        genTfUserId.clear();
        genComboBoxTaskType.getSelectionModel().clearSelection();
        genDtDeadlineFrom.getEditor().clear();
        genDtDeadlineTo.getEditor().clear();

        taskTableView.getItems().removeAll();
        taskTableView.setItems(tasks);
    }

    private void handleChoiceBoxFilterChange(String filter) {
        if (Arrays.asList(SINGLE_TEXT_FIELD_FILTERS).contains(filter)) {
            if (!SINGLE_FILTER_ACTIVE) {
                SINGLE_FILTER_ACTIVE = true;
                RANGE_FILTER_ACTIVE = false;
                DROPDOWN_FILTER_ACTIVE = false;
                // Cleanup
                singleFilterView.getChildren().removeAll(dtFrom, dtTo, comboBoxTaskType, btnSearch, btnClear);

                // set new fields
                tfSearchQuery = new TextField();
                tfSearchQuery.setPromptText("Enter " + filter);
                tfSearchQuery.setMinWidth(100);

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                singleFilterView.getChildren().addAll(tfSearchQuery, btnSearch, btnClear);
            } else {
                tfSearchQuery.setPromptText("Enter " + filter);
            }
        } else if (filter == "Task Type") {
            if (!DROPDOWN_FILTER_ACTIVE) {
                DROPDOWN_FILTER_ACTIVE = true;
                RANGE_FILTER_ACTIVE = false;
                SINGLE_FILTER_ACTIVE = false;

                // Cleanup
                singleFilterView.getChildren().removeAll(dtFrom, dtTo, tfSearchQuery, btnSearch, btnClear);

                comboBoxTaskType = new ComboBox<>();
                comboBoxTaskType.setPromptText("Choose a Filter...");
                comboBoxTaskType.getItems().addAll("FrontEnd", "BackEnd", "QA");

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                singleFilterView.getChildren().addAll(comboBoxTaskType, btnSearch, btnClear);
            }
        } else {
            if (!RANGE_FILTER_ACTIVE) {
                RANGE_FILTER_ACTIVE = true;
                SINGLE_FILTER_ACTIVE = false;
                DROPDOWN_FILTER_ACTIVE = false;
                // Cleanup
                singleFilterView.getChildren().removeAll(tfSearchQuery, comboBoxTaskType, btnSearch, btnClear);

                dtFrom = new DatePicker();
                dtFrom.setPromptText("From Date");
                dtTo = new DatePicker();
                dtTo.setPromptText("To Date");

                // Search and Clear buttons
                btnSearch = new Button("Search");
                btnSearch.setOnAction(e -> handleSearch());
                btnClear = new Button("Clear");
                btnClear.setOnAction(e -> handleClear());

                singleFilterView.getChildren().addAll(dtFrom, dtTo, btnSearch, btnClear);
            }
        }
    }

    private void handleSearch() {
        if (SINGLE_FILTER_ACTIVE) {
            String query = tfSearchQuery.getText();
            String filterName = comboBoxFilter.getValue();
            List<Task> filtered = null;
            //"Project Name", "Task Type", "User ID"
            switch (filterName) {
                case "Project Name": {
                    filtered = getFilteredTaskList((List) tasks, findByProjectName(query));
                    break;
                }
                case "User ID": {
                    filtered = getFilteredTaskList((List) tasks, findByUserId(query));
                    break;
                }
                default: {
                    filtered = (List) tasks;
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

            if (filterName == "Deadline") {
                filtered = getFilteredTaskList((List) tasks, findByDeadline(ldtFrom, ldtTo));
            } else {
                filtered = getFilteredTaskList((List) tasks, findByCompletionDate(ldtFrom, ldtTo));
            }

            filteredTasks = FXCollections.observableArrayList(filtered);
            taskTableView.getItems().removeAll();
            taskTableView.setItems(filteredTasks);
        } else if (DROPDOWN_FILTER_ACTIVE) {
            String strTaskType = comboBoxTaskType.getValue();
            TaskType taskType = TaskType.valueOf(strTaskType.toUpperCase());

            List<Task> filtered = getFilteredTaskList((List) tasks, findByTaskType(taskType));

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
        if (DROPDOWN_FILTER_ACTIVE) {
            comboBoxTaskType.getSelectionModel().clearSelection();
        } else if (RANGE_FILTER_ACTIVE) {
            dtFrom.getEditor().clear();
            dtTo.getEditor().clear();
        } else {
            tfSearchQuery.clear();
        }

        taskTableView.getItems().removeAll();
        taskTableView.setItems(tasks);
    }
}

