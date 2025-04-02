package vacancy_manager.repos;



import vacancy_manager.models.Role;
import vacancy_manager.models.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepo extends UnicastRemoteObject implements vacancy_manager.rmi_interfaces.LoginRepo {

    public LoginRepo() throws RemoteException {
        super(); // экспортирует объект для RMI
    }

    @Override
    public User login(User user) throws RemoteException {
        String query = "SELECT id, role FROM \"user\" WHERE login = ? AND password = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Устанавливаем параметры запроса
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Если пользователь найден, устанавливаем его роль
                    user.setId(resultSet.getInt("id"));
                    String roleStr = resultSet.getString("role");
                    Role role = Role.valueOf(roleStr.toUpperCase());
                    user.setRole(role);
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Ошибка при проверке логина и пароля", e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RemoteException("Неверная роль пользователя в базе данных", e);
        }

        return null;
    }
}