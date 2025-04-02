package vacancy_manager;

import vacancy_manager.repos.CandidateRepo;
import vacancy_manager.repos.LoginRepo;
import vacancy_manager.repos.ManagerRepo;
import vacancy_manager.repos.VacancyRepo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            VacancyRepo vacancyRepo = new VacancyRepo();
            ManagerRepo managerRepo = new ManagerRepo();
            CandidateRepo candidateRepo = new CandidateRepo();
            LoginRepo loginRepo = new LoginRepo();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.bind("VacancyRepo", vacancyRepo);
            registry.bind("ManagerRepo", managerRepo);
            registry.bind("CandidateRepo", candidateRepo);
            registry.bind("LoginRepo", loginRepo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
