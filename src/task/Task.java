/*
 * @author Abhishek Ghosh
 */
package task;

import java.time.LocalDateTime;
import java.util.Random;

public class Task {
    public enum TASK_TYPES {
        FRONTEND,
        BACKEND,
        QA;

        public static TASK_TYPES getRandomTaskType() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    };

    private String projectName;
    private String taskName;
    private Integer taskId;
    private TASK_TYPES taskType;
    private  String taskDescription;
    private  String userId;
    private LocalDateTime taskDeadline;
    private LocalDateTime taskCompletedOn;

    public Task(String projectName, String taskName, Integer taskId, TASK_TYPES taskType, String taskDescription, String userId, LocalDateTime taskDeadline) {
        this.projectName = projectName;
        this.taskName = taskName;
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskDescription = taskDescription;
        this.userId = userId;
        this.taskDeadline = taskDeadline;
    }

    @Override
    public String toString() {
        return "Task{" +
                "projectName='" + projectName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskId=" + taskId +
                ", taskType=" + taskType +
                ", taskDescription='" + taskDescription + '\'' +
                ", userId='" + userId + '\'' +
                ", taskDeadline=" + taskDeadline +
                ", taskCompletedOn=" + taskCompletedOn +
                '}';
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public TASK_TYPES getTaskType() {
        return taskType;
    }

    public void setTaskType(TASK_TYPES taskType) {
        this.taskType = taskType;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(LocalDateTime taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public LocalDateTime getTaskCompletedOn() {
        return taskCompletedOn;
    }

    public void setTaskCompletedOn(LocalDateTime taskCompletedOn) {
        this.taskCompletedOn = taskCompletedOn;
    }
}
