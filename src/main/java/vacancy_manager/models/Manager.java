package vacancy_manager.models;

import java.io.Serial;
import java.io.Serializable;

public class Manager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String phone;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;

    public Manager(int id, String firstName, String lastName, String patronymic, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        if (firstName == null || lastName == null) {
            return "";
        }
        return firstName + " " + lastName + (patronymic != null ? " " + patronymic : "");
    }
}