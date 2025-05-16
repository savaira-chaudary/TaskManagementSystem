package taskmanagementsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Entity Layer
class Task {
    private static int counter = 1;
    private int id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;

    public Task(String title, String description, LocalDate dueDate) {
        this.id = counter++;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void markCompleted() { this.completed = true; }
    public void markIncomplete() { this.completed = false; }

    @Override
    public String toString() {
        return String.format("ID: %d\nTitle: %s\nDescription: %s\nDue Date: %s\nStatus: %s\n",
                id, title, description, dueDate, completed ? "Completed" : "Incomplete");
    }
}
// Service Layer
class TaskService {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void editTask(int id, String title, String description, LocalDate dueDate) {
        Task task = findTaskById(id);
        if (task != null) {
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
        }
    }

    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
    }

    public void markTaskCompleted(int id) {
        Task task = findTaskById(id);
        if (task != null) task.markCompleted();
    }

    public void markTaskIncomplete(int id) {
        Task task = findTaskById(id);
        if (task != null) task.markIncomplete();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByStatus(boolean completed) {
        return tasks.stream().filter(t -> t.isCompleted() == completed).collect(Collectors.toList());
    }

    public List<Task> getTasksByDueDate(LocalDate date) {
        return tasks.stream().filter(t -> t.getDueDate().equals(date)).collect(Collectors.toList());
    }

    private Task findTaskById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }
}
// UI Layer
class ConsoleUI {
    private Scanner scanner = new Scanner(System.in);
    private TaskService taskService = new TaskService();

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Task Management System ---");
            System.out.println("1. Add Task");
            System.out.println("2. Edit Task");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task as Complete");
            System.out.println("5. Mark Task as Incomplete");
            System.out.println("6. View All Tasks");
            System.out.println("7. Filter Tasks by Status");
            System.out.println("8. Filter Tasks by Due Date");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": addTask(); break;
                case "2": editTask(); break;
                case "3": deleteTask(); break;
                case "4": markCompleted(); break;
                case "5": markIncomplete(); break;
                case "6": viewAllTasks(); break;
                case "7": filterByStatus(); break;
                case "8": filterByDueDate(); break;
                case "9": System.out.println("Exiting..."); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private void addTask() {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Due Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        taskService.addTask(new Task(title, desc, date));
        System.out.println("Task added.");
    }

    private void editTask() {
        int id = readId();
        System.out.print("New Title: ");
        String title = scanner.nextLine();
        System.out.print("New Description: ");
        String desc = scanner.nextLine();
        System.out.print("New Due Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        taskService.editTask(id, title, desc, date);
        System.out.println("Task updated.");
    }

    private void deleteTask() {
        int id = readId();
        taskService.deleteTask(id);
        System.out.println("Task deleted.");
    }

    private void markCompleted() {
        int id = readId();
        taskService.markTaskCompleted(id);
        System.out.println("Task marked as complete.");
    }

    private void markIncomplete() {
        int id = readId();
        taskService.markTaskIncomplete(id);
        System.out.println("Task marked as incomplete.");
    }

    private void viewAllTasks() {
        taskService.getAllTasks().forEach(System.out::println);
    }

    private void filterByStatus() {
        System.out.print("Enter status (completed/incomplete): ");
        String status = scanner.nextLine();
        boolean completed = status.equalsIgnoreCase("completed");
        taskService.getTasksByStatus(completed).forEach(System.out::println);
    }

    private void filterByDueDate() {
        System.out.print("Enter due date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        taskService.getTasksByDueDate(date).forEach(System.out::println);
    }

    private int readId() {
        System.out.print("Enter Task ID: ");
        return Integer.parseInt(scanner.nextLine());
    }
}
// Main class
public class TaskManagementSystem {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.displayMenu();
    }
}

