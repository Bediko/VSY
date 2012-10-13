package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client_conn extends Remote {
	void getMessage(String message)
		throws RemoteException;
}
