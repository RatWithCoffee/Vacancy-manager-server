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
        String query = "SELECT m.*, u.login, u.password " +
                "FROM manager m " +
                "JOIN \"user\" u ON m.id = u.id " +
                "WHERE u.role = CAST(? AS user_role)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "manager");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Manager manager = new Manager(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("patronymic"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getString("login"),
                            resultSet.getString("password")
                    );
                    managers.add(manager);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return managers;
    }

    public int addManager(Manager manager) {
        Connection connection = null;
        int generatedId = -1;

        try {
            connection = DbManager.getConnection();
            connection.setAutoCommit(false); // Начало транзакции

            // Первый запрос - добавление пользователя
            String userQuery = "INSERT INTO \"user\" (role, login, password) VALUES ('manager', ?, ?) RETURNING id";
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                userStatement.setString(1, manager.getLogin());
                userStatement.setString(2, manager.getPassword()); // Исправлено: было setString(1, ...)

                try (ResultSet resultSet = userStatement.executeQuery()) {
                    if (resultSet.next()) {
                        manager.setId(resultSet.getInt("id"));
                    }
                }
            }

            // Второй запрос - добавление менеджера
            String managerQuery = "INSERT INTO manager (id, first_name, last_name, patronymic, email, phone) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement managerStatement = connection.prepareStatement(managerQuery)) {
                managerStatement.setInt(1, manager.getId());
                managerStatement.setString(2, manager.getFirstName());
                managerStatement.setString(3, manager.getLastName());
                managerStatement.setString(4, manager.getPatronymic());
                managerStatement.setString(5, manager.getEmail());
                managerStatement.setString(6, manager.getPhone());

                try (ResultSet resultSet = managerStatement.executeQuery()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt("id");
                    }
                }
            }

            connection.commit(); // Подтверждение транзакции
            return generatedId;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Откат при ошибке
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Восстановление авто-коммита
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateManager(Manager manager) {
        Connection connection = null;
        try {
            connection = DbManager.getConnection();
            // Start transaction
            connection.setAutoCommit(false);

            // First query - update user credentials
            String userQuery = "UPDATE \"user\" SET login = ?, password = ? WHERE id = ?";
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery)) {
                userStatement.setString(1, manager.getLogin());
                userStatement.setString(2, manager.getPassword());
                userStatement.setInt(3, manager.getId());
                userStatement.executeUpdate();
            }

            // Second query - update manager details
            String managerQuery = "UPDATE manager SET first_name = ?, last_name = ?, patronymic = ?, email = ?, phone = ? WHERE id = ?";
            try (PreparedStatement managerStatement = connection.prepareStatement(managerQuery)) {
                managerStatement.setString(1, manager.getFirstName());
                managerStatement.setString(2, manager.getLastName());
                managerStatement.setString(3, manager.getPatronymic());
                managerStatement.setString(4, manager.getEmail());
                managerStatement.setString(5, manager.getPhone());
                managerStatement.setInt(6, manager.getId());
                managerStatement.executeUpdate();
            }

            // Commit transaction
            connection.commit();
        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
