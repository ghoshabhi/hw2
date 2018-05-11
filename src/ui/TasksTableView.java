package ui;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import task.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TasksTableView {
    public static TableView getTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");

        TableColumn<Task, Integer> colTaskId = new TableColumn<>("TaskId");
        colTaskId.setMinWidth(60);
        colTaskId.setStyle("-fx-alignment: CENTER");
        colTaskId.setCellValueFactory(new PropertyValueFactory<>("taskId"));

        TableColumn<Task, String> colTaskName = new TableColumn<>("Task Name");
        colTaskName.setMinWidth(150);
        colTaskName.setStyle("-fx-alignment: CENTER");
        colTaskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<Task, String> colProjectName = new TableColumn<>("Project Name");
        colProjectName.setMinWidth(150);
        colProjectName.setStyle("-fx-alignment: CENTER");
        colProjectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));

        TableColumn<Task, String> colTaskType = new TableColumn<>("Task Type");
        colTaskType.setMinWidth(150);
        colTaskType.setStyle("-fx-alignment: CENTER");
        colTaskType.setCellValueFactory(new PropertyValueFactory<>("taskType"));

        TableColumn<Task, String> colUserId = new TableColumn<>("User ID");
        colUserId.setMinWidth(60);
        colUserId.setStyle("-fx-alignment: CENTER");
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserId.setCellFactory((TableColumn<Task, String> column) -> {
            return new TableCell<Task, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });

        TableColumn<Task, LocalDateTime> colTaskDeadline = new TableColumn<>("Deadline");
        colTaskDeadline.setMinWidth(150);
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
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });

        TableColumn<Task, LocalDateTime> colTaskCompleted = new TableColumn<>("Completed");
        colTaskCompleted.setMinWidth(150);
        colTaskCompleted.setStyle("-fx-alignment: CENTER");
        colTaskCompleted.setCellValueFactory(new PropertyValueFactory<>("taskCompletedOn"));
        colTaskCompleted.setCellFactory((TableColumn<Task, LocalDateTime> column) -> {
            return new TableCell<Task, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });

        TableView taskTableView = new TableView<>();
        taskTableView.setPlaceholder(new Label("No Records!"));
        taskTableView.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        taskTableView.getColumns().addAll(colTaskId, colTaskName, colProjectName, colTaskType, colUserId, colTaskDeadline, colTaskCompleted);

        return taskTableView;
        // taskTableView.setItems(getTasks());
    };
}
