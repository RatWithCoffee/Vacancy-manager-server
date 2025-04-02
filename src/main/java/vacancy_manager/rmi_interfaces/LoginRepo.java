package vacancy_manager.rmi_interfaces;


import vacancy_manager.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoginRepo extends Remote {
    User login(User user) throws RemoteException;
}
