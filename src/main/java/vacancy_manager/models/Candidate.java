package vacancy_manager.models;

import java.io.Serial;
import java.io.Serializable;

public class Candidate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;               // Уникальный идентификатор кандидата
    private String firstName;     // Имя кандидата
    private String lastName;      // Фамилия кандидата
    private String patronymic;    // Отчество кандидата
    private String email;         // Электронная почта кандидата
    private String phone;         // Телефон кандидата
    private String cv;            // Ссылка на резюме кандидата
    private int vacancyId;        // ID вакансии, на которую кандидат подал заявку

    // Конструктор без параметров
    public Candidate() {
    }

    // Конструктор с параметрами
    public Candidate(int id, String firstName, String lastName,
                     String patronymic, String email, String phone,
                     String cv, int vacancyId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.cv = cv;
        this.vacancyId = vacancyId;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public int getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(int vacancyId) {
        this.vacancyId = vacancyId;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", cv='" + cv + '\'' +
                ", vacancyId=" + vacancyId +
                '}';
    }
}