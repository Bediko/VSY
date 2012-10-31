import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;


public class Server extends UnicastRemoteObject implements ServerInterface {
	private HashMap<String, ClientInterface> _userStore;
	private String[] names;
	private boolean secondary;
	private static Server server;

	public Server() throws RemoteException {
		_userStore = new HashMap<String, ClientInterface>();
		secondary = false;
	}
	
	@Override
	//Angemeldeten User speichern und begrüßen
	public void register(String userName, ClientInterface clientObject) {
		_userStore.put(userName, clientObject);
		
		try {
			clientObject.notifyMessage("Server", "Welcome " + userName);
		} catch(RemoteException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	//User abmelden
	public void unregister(String username) {
		_userStore.remove(username);
	}
	
	@Override
	public boolean ping(){
		return true;
	}

	@Override
	// alle angemeldeten User abfragen
	public String[] getAllUser() {
		return _userStore.keySet().toArray(new String[0]);
	}

	@Override
	//Nachricht von User an anderen User weiterleiten
	public void sendMessage(String sender, String receiver, String message) throws RemoteException {
		ClientInterface client = _userStore.get(receiver);
		
		if(client != null)
			client.notifyMessage(sender, message);
	}
	
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
