package vacancy_manager.repos;


import vacancy_manager.models.Manager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerRepo extends UnicastRemoteObject implements vacancy_manager.rmi_interfaces.ManagerRepo {
    public ManagerRepo() throws RemoteException {
        super(); // экспортирует объект для RMI
    }

    public List<Manager> getAll() {
        List<Manager> managers = new ArrayList<>();
        String query = "SELECT * FROM manager";

        try (Connection connection = DbManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String patronymic = resultSet.getString("patronymic");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                Manager manager = new Manager(id, firstName, lastName, patronymic, email, phone);
                managers.add(manager);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return managers;
    }

    public int addManager(Manager manager) {
        String query = "INSERT INTO \"user\" (role) VALUES ('manager') RETURNING id";
        try (Connection connection = DbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                   manager.setUserId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

         query = "INSERT INTO manager (first_name, last_name, patronymic, email, phone, user_id) VALUES (?, ?, ?, ?, ?,?) RETURNING id";
        int generatedId = -1;  // Значение по умолчанию, если что-то пойдет не так

        try (Connection connection = DbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, manager.getFirstName());
            preparedStatement.setString(2, manager.getLastName());
            preparedStatement.setString(3, manager.getPatronymic());
            preparedStatement.setString(4, manager.getEmail());
            preparedStatement.setString(5, manager.getPhone());
            preparedStatement.setInt(6, manager.getUserId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    generatedId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public void updateManager(Manager manager) {
        String query = "UPDATE manager SET first_name = ?, last_name = ?, patronymic = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, manager.getFirstName());
            preparedStatement.setString(2, manager.getLastName());
            preparedStatement.setString(3, manager.getPatronymic());
            preparedStatement.setString(4, manager.getEmail());
            preparedStatement.setString(5, manager.getPhone());
            preparedStatement.setInt(6, manager.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteManager(int id) {
        String query = "DELETE FROM manager WHERE id = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
