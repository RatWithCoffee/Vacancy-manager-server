package vacancy_manager.rmi_interfaces;



import vacancy_manager.models.Vacancy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VacancyRepo extends Remote {

    // Method to get all vacancies
    List<Vacancy> getAllVacancies() throws RemoteException;
    List<Vacancy> getVacanciesByManager(int managerId) throws RemoteException;


    // Method to add a new vacancy and return its ID
    int addVacancy(Vacancy vacancy) throws RemoteException;

    // Method to update an existing vacancy
    boolean updateVacancy(Vacancy vacancy) throws RemoteException;

    // Method to delete a vacancy by ID
    boolean deleteVacancy(int id) throws RemoteException;

    List<Map<String, Object>> getVacanciesWithSalaries() throws RemoteException;

    Map<String, Integer> getNumberOfCandidatesToVac() throws RemoteException;
}
