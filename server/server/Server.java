/**
 * 
 */
package server;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;




public class Server extends UnicastRemoteObject implements Client_conn {

	public Server() throws RemoteException {
		super();	// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setSecurityManager (new RMISecurityManager ());
		Server server = null;
		try {
			server = new Server ();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			Naming.bind ("rmi://127.0.0.1:9090/server",server);
		}catch(AlreadyBoundException ex){
			try {
				Naming.rebind ("rmi://127.0.0.1:9090/server",server);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage ());
		}

	}
	@Override
	public void getMessage(String message) throws RemoteException {
		System.out.println(message);	
	}

}
