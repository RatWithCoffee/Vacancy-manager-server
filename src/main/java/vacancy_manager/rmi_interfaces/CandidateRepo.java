package vacancy_manager.rmi_interfaces;


import vacancy_manager.models.Candidate;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CandidateRepo extends Remote {
    List<Candidate> getCandidatesByVacancyId(int vacancyId) throws RemoteException;
    int addCandidatesBatch(List<Candidate> candidates, int vacancyId) throws RemoteException;
}
