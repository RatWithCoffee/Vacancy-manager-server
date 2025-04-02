package vacancy_manager.repos;

import vacancy_manager.models.Candidate;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidateRepo extends UnicastRemoteObject implements vacancy_manager.rmi_interfaces.CandidateRepo {
    public CandidateRepo() throws RemoteException {
        super(); // экспортирует объект для RMI
    }


    // Метод для получения списка кандидатов по ID вакансии
    public List<Candidate> getCandidatesByVacancyId(int vacancyId) {
        String sql = "SELECT id, first_name, last_name, patronymic, email, phone, cv " +
                "FROM candidate WHERE vacancy_id = ?";

        List<Candidate> candidates = new ArrayList<>();

        try (Connection conn = DbManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Устанавливаем ID вакансии в запрос
                stmt.setInt(1, vacancyId);

                // Выполняем запрос
                try (ResultSet rs = stmt.executeQuery()) {
                    // Обрабатываем результаты запроса
                    while (rs.next()) {
                        Candidate candidate = new Candidate();
                        candidate.setId(rs.getInt("id"));
                        candidate.setFirstName(rs.getString("first_name"));
                        candidate.setLastName(rs.getString("last_name"));
                        candidate.setPatronymic(rs.getString("patronymic"));
                        candidate.setEmail(rs.getString("email"));
                        candidate.setPhone(rs.getString("phone"));
                        candidate.setCv(rs.getString("cv"));
                        candidate.setVacancyId(vacancyId);  // Устанавливаем ID вакансии

                        candidates.add(candidate);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return candidates;  // Возвращаем список кандидатов
    }

    public int addCandidatesBatch(List<Candidate> candidates, int vacancyId) {
        String sql = "INSERT INTO candidate (first_name, last_name, patronymic, email, phone, cv, vacancy_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int[] results;
        int successfulInserts = 0;

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Отключаем авто-коммит для пакетной обработки
            conn.setAutoCommit(false);

            for (Candidate candidate : candidates) {
                stmt.setString(1, candidate.getFirstName());
                stmt.setString(2, candidate.getLastName());
                stmt.setString(3, candidate.getPatronymic());
                stmt.setString(4, candidate.getEmail());
                stmt.setString(5, candidate.getPhone());
                stmt.setString(6, candidate.getCv());
                stmt.setInt(7, vacancyId);
                stmt.addBatch();
            }

            results = stmt.executeBatch();
            conn.commit();

            // Подсчитываем успешные вставки
            for (int result : results) {
                if (result >= 0) { // SUCCESS_NO_INFO или количество измененных строк
                    successfulInserts++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return successfulInserts;
    }

}
