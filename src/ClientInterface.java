
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientInterface extends Remote {
	public void notifyMessage(String sender, String message) throws RemoteException;
	
//####################################################################################
//####################### only relevant for the GUI ##################################
//####################################################################################	
	public void updateUserList(String[] users) throws RemoteException;
	public String getPass() throws RemoteException;
//####################################################################################
//####################################################################################
//####################################################################################	
}
