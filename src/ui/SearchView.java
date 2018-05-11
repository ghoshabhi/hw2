package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import task.Task;
import taskpredicates.TaskPredicates;

import java.util.List;

public class SearchView {
    TextField tfTaskId, tfTaskName, tfProjectName;
    HBox searchBoxView;
    public HBox getSearchView() {
        // TaskID Input
        tfTaskId = new TextField();
        tfTaskId.setPromptText("Task ID");
        tfTaskId.setMinWidth(100);

        // Task Name Input
        tfTaskName = new TextField();
        tfTaskName.setPromptText("Task Name");
        tfTaskName.setMinWidth(100);

        // Task Name Input
        tfProjectName = new TextField();
        tfProjectName.setPromptText("Project Name");
        tfProjectName.setMinWidth(100);

        // Search Button
        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> handleSearch());

        searchBoxView = new HBox(10);
        searchBoxView.setPadding(new Insets(10,10,10,10));
        // tfTaskId, tfTaskName,
        searchBoxView.getChildren().addAll(tfProjectName, btnSearch);
        return searchBoxView;
    }

        public void handleSearch() { // needs List<Task> tasks
//        String taskName = tfProjectName.getText();
//        List<Task> filtered = TaskPredicates.getFilteredTaskList((List)tasks, TaskPredicates.findByProjectName(taskName));
//        System.out.println("filtered = " + filtered);
    }

}
