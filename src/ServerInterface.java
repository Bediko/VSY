import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServerInterface extends Remote {
	public boolean register(String userName, ClientInterface clientObject) throws RemoteException;
	public void unregister(String userName) throws RemoteException;
	public String[] getAllUser() throws RemoteException;
	public void sendMessage(String sender, String receiver, String message) throws RemoteException;
	public boolean ping() throws RemoteException;
}
