import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
/**
 * A server implementation of an instant messenger.
 * @author Michael, Simon
 *
 */

public class Server extends UnicastRemoteObject implements ServerInterface {
	private HashMap<String, ClientInterface> _userStore;
	private String[] names;
	private boolean secondary;
	private static Server server;

	public Server() throws RemoteException {
		_userStore = new HashMap<String, ClientInterface>();
		secondary = false;
	}
	
	/**
	 * Registers user on the server and greets them
	 * @param userName Name of the user which will be registered
	 * @param clientObject The client interface which calls the register method
	 */
	@Override
	public void register(String userName, ClientInterface clientObject) {
		_userStore.put(userName, clientObject);
		
		try {
			clientObject.notifyMessage("Server", "Welcome " + userName);
		} catch(RemoteException ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * Unregisters a user from the server.
	 * @param username Name of the user which will be unregistered
	 */
	@Override	
	public void unregister(String username) {
		_userStore.remove(username);
	}
	
	/**
	 * Function to check, if the other server is online and responding
	 */
	@Override
	public boolean ping(){
		return true;
	}

	/**
	 * Gets all users which are registered at the server.
	 * @return Array of Strings with the user names
	 */
	@Override
	public String[] getAllUser() {
		return _userStore.keySet().toArray(new String[0]);
	}
	
	/**
	 * Gets a message from a client and sends it to another client
	 * @param sender User name who sends the message
	 * @param receiver User name to where the message should be delivered
	 * @param message The message which should be send
	 */
	@Override
	public void sendMessage(String sender, String receiver, String message) throws RemoteException {
		ClientInterface client = _userStore.get(receiver);
		
		if(client != null)
			client.notifyMessage(sender, message);
	}
	/**
	 * Determinate if the server will be the primary or the secondary server
	 * @return True when Server is secondary, false otherwise
	 */
	public boolean is_secondary(){
		try{
			ServerInterface remoteObj = (ServerInterface) Naming.lookup("rmi://127.0.0.1:9090/server1");
			if(remoteObj.ping()){
				secondary=true;
			}
			return secondary;
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return secondary;
		}
	};
	/**
	 * Registers the server at the rmi registry. 
	 * The binded address depends if the server is secondary or not.
	 */
	public void connect(){
		try {
			if(!is_secondary()){
				Naming.rebind("rmi://127.0.0.1:9090/server1", server);
			}
			else{
				try {
					ServerInterface remoteObj = (ServerInterface) Naming.lookup("rmi://127.0.0.1:9090/server2");
					remoteObj.ping();
					System.exit(0);
				}
				catch(Exception ex){
				Naming.rebind("rmi://127.0.0.2:9090/server2", server);
				}
			}
			
			System.out.println("Server binded object successfull!");	
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
			System.exit(0);
		}
		
	};
	
	
	/**
	 * 
	 * Main function which will connect the server and informs the admin in intervals which users are 
	 * online
	 */
	public static void main(String[] args) {
		System.setSecurityManager(new SecurityManager());
		
		try {
			server = new Server();
			server.connect();
			while(server._userStore.keySet().toArray(new String[0]).length == 0) {
				System.out.println("No clients registered!");
				Thread.sleep(5000);
			}
			
			while(true) {
				System.out.println("Users registered: " + Arrays.toString(server._userStore.keySet().toArray(new String[0])));
				Thread.sleep(15000);
			}
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
