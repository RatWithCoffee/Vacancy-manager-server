package vacancy_manager.repos;
import vacancy_manager.models.Vacancy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VacancyRepo extends UnicastRemoteObject implements vacancy_manager.rmi_interfaces.VacancyRepo {

    public VacancyRepo() throws RemoteException {
        super(); // экспортирует объект для RMI
    }

    public List<Vacancy> getVacanciesByManager(int managerId) {
        List<Vacancy> vacancies = new ArrayList<>();
        try (Connection conn = DbManager.getConnection()) {
            String sql = "SELECT vacancy.id, title, description, salary, manager_id, first_name, last_name" +
                    " FROM vacancy LEFT JOIN manager ON vacancy.manager_id = manager.id WHERE manager_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ) {
                stmt.setInt(1, managerId);
                ResultSet rs = stmt.executeQuery();


                while (rs.next()) {
                    Vacancy vacancy = mapResultSetToVacancy(rs);
                    vacancies.add(vacancy);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return vacancies;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vacancies;
    }

    // Method to get all vacancies
    public  List<Vacancy> getAllVacancies() {
        List<Vacancy> vacancies = new ArrayList<>();
        try (Connection conn = DbManager.getConnection()) {
            String sql = "SELECT vacancy.id, title, description, salary, manager_id, first_name, last_name" +
                    " FROM vacancy LEFT JOIN manager ON vacancy.manager_id = manager.id";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Vacancy vacancy = mapResultSetToVacancy(rs);
                    System.out.println(vacancy);
                    vacancies.add(vacancy);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return vacancies;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vacancies;
    }


    // Method to add a new vacancy and return its ID
    public  int addVacancy(Vacancy vacancy) {
        String sql = "INSERT INTO vacancy (title, description, salary, manager_id) VALUES (?, ?, ?, ?) RETURNING id";
        int generatedId = -1;  // Default value if insertion fails
        System.out.println("new vac");
        System.out.println(vacancy);

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vacancy.getTitle());
            stmt.setString(2, vacancy.getDescription());
            stmt.setDouble(3, vacancy.getSalary());

            if (vacancy.getManagerId() == null) {
                stmt.setNull(4, java.sql.Types.INTEGER);  // Указываем тип NULL значения
            } else {
                stmt.setInt(4, vacancy.getManagerId());
            }


            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    generatedId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;  // Returns -1 if insertion failed
    }

    // Method to update an existing vacancy
    public boolean updateVacancy(Vacancy vacancy) {
        String sql = "UPDATE vacancy SET title = ?, description = ?, salary = ?, manager_id = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, vacancy.getTitle());
                stmt.setString(2, vacancy.getDescription());
                stmt.setDouble(3, vacancy.getSalary());
                stmt.setInt(4, vacancy.getManagerId());
                stmt.setInt(5, vacancy.getId());

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;  // Return true if update was successful
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Return false if there was an error
    }

    // Method to delete a vacancy by ID
    public  boolean deleteVacancy(int id) {
        String sql = "DELETE FROM vacancy WHERE id = ?";
        try (Connection conn = DbManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;  // Return true if delete was successful
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if there was an error
    }

    // Helper method to map a ResultSet row to a Vacancy object
    private static Vacancy mapResultSetToVacancy(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        double salary = rs.getDouble("salary");
        Integer managerId = rs.getInt("manager_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");


        if (firstName == null) {
            return new Vacancy(id, title, description, salary, managerId, " ");
        }
        return new Vacancy(id, title, description, salary, managerId, firstName + " " + lastName);
    }


    public List<Map<String, Object>> getVacanciesWithSalaries() {
        List<Map<String, Object>> vacancies = new ArrayList<>();

        String query = "SELECT title, salary FROM vacancy ORDER BY salary DESC";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> vacancy = new HashMap<>();
                vacancy.put("title", resultSet.getString("title"));
                vacancy.put("salary", resultSet.getDouble("salary"));
                vacancies.add(vacancy);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vacancies;
    }

    public Map<String, Integer> getNumberOfCandidatesToVac() {
        Map<String, Integer> managerVacancies = new HashMap<>();

        String query = "SELECT v.title, COUNT(*) AS candidates_count FROM vacancy v JOIN candidate c ON v.id = c.vacancy_id GROUP BY(v.title)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                managerVacancies.put(
                        resultSet.getString("title"),
                        resultSet.getInt("candidates_count")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return managerVacancies;
    }


}
