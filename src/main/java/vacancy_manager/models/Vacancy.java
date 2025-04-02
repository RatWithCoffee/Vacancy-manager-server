package vacancy_manager.models;

import java.io.Serial;
import java.io.Serializable;

public class Vacancy implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String description;
    private double salary;
    private String managerName;
    private Integer managerId;

    public Vacancy(int id, String title, String description, double salary, Integer managerId, String managerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.managerId = managerId;
        this.managerName = managerName;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getSalary() {
        return salary;
    }

    public String getManagerName() {
        return managerName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", salary=" + salary +
                ", managerName='" + managerName + '\'' +
                ", managerId=" + managerId +
                '}';
    }
}