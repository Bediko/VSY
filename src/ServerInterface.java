import java.rmi.Remote;
import java.rmi.RemoteException;




public interface ServerInterface extends Remote {
	public static final int CLIENT = 0;
	public static final int SERVER = 1;
	public boolean newUser(String userName, String password, int sentBy) throws RemoteException;
	public boolean login(String userName, String password, ClientInterface clientObject, int sentBy) throws RemoteException;
	public void logout(String userName, int sentBy) throws RemoteException;
	public String[] getAllUser() throws RemoteException;
	public void sendMessage(String sender, String receiver, String message, int sentBy) throws RemoteException;
	public boolean ping() throws RemoteException;
	public void requestInit() throws RemoteException;
	public boolean addBuddy(String user, String buddy, int sentBy) throws RemoteException;
	public String[] getBuddies(String user) throws RemoteException;
	public String[] getMessages(String user) throws RemoteException;
}
