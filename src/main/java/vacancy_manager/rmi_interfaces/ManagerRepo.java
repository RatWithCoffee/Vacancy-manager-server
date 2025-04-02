package vacancy_manager.rmi_interfaces;


import vacancy_manager.models.Manager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ManagerRepo  extends Remote {
    List<Manager> getAll() throws RemoteException;
    int addManager(Manager manager) throws RemoteException;
    void updateManager(Manager manager) throws RemoteException;
    void deleteManager(int id) throws RemoteException;
}
