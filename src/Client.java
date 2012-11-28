
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Client implementation of a instant messenger
 *
 */
public class Client implements ClientInterface {
	private static Client _instance;
	ServerInterface _remoteObj;
	String _username, _receiver;
	public Scanner _scanner;
	
	public Client() {
		_scanner = new Scanner(System.in);
		try {
			UnicastRemoteObject.exportObject(this, 0);
			_instance = this;
		} catch(RemoteException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Delivers a message to the User, sent by another User
	 * @param sender The sender of the Message
	 * @param message The text of the Message
	 */
	@Override
	public void notifyMessage(String sender, String message) {
		System.out.println("\n++++> " + sender + " says " + message);		
	}
	
	/**
	 *try to connect to one of the server. 3 retries for each.
	 *@return Returns true if the connection was successful, else the returnvalue is false
	 */
	public boolean connect() {
		for(int i = 0; i < 3; i++) {
			try {
				_remoteObj = (ServerInterface) Naming.lookup("rmi://127.0.0.1:9090/server1");
				_remoteObj.ping();
				System.out.println("Connected to Server 1");
				return true;
			} catch(Exception ex) {
				System.out.println("Server 1 is not responding. \nRetrying to connect ...");
			}
		}
		for(int i = 0; i < 3; i++) {
			try {
				_remoteObj = (ServerInterface) Naming.lookup("rmi://127.0.0.2:9090/server2");
				_remoteObj.ping();
				System.out.println("Connected to Server 2");
				return true;
			} catch(Exception ex) {
				System.out.println("Server 2 is not responding. \nRetrying to connect ...");
			}		
		}
		return false;
	}
	
	
	/**
	 * Prompts a Login dialog and tries to login on the server
	 * @return Returns true if login was successful, else the returnvalue is false
	 */
	public boolean login() {
		boolean loggedIn = false;
		
		try {			
			while(loggedIn == false) {
				System.out.println("Username: ");
				_username = _scanner.next();
				loggedIn = _remoteObj.register(_username, new Client(), ServerInterface.CLIENT);
			}
		} catch(Exception ex){
			if(connect())
				return login();
			System.out.println("A Login-Error occured: " + ex.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Prompts the User Interface
	 */
	public void paintUserInterface() {
		try {
			System.out.println("Users registered: " + Arrays.toString(_remoteObj.getAllUser()));
		} catch(Exception ex)  {
			System.out.println("Cannot get Userlist!");
		}
		System.out.println("exit - exit from commandline");
		System.out.println("list - list all users");
		System.out.println("chuser - change user");		
	}
	
	
	/**
	 * @return Returns all online Users as an Array of Strings, or null if an Error occurred 
	 */
	public String[] getUserList() {
		try {
			return _remoteObj.getAllUser();
		} catch(RemoteException ex) {
			if(connect())
				return getUserList();
			System.out.println("Cannot get UserList!");
		}
		return null;
	}
	
	/**
	 * Change the User you want to chat with
	 * @param receiver Identify the User, you want to chat with
	 * @return Returns true if the User was found and false if the User was not found
	 */
	public boolean changeUser(String receiver) {
		try {
			for(String user : _remoteObj.getAllUser()) {
				if(user.equalsIgnoreCase(receiver)) {
					_receiver = receiver;
					return true;
				}
			}
		} catch(RemoteException ex) {
			if(connect())
				return changeUser(receiver);
			System.out.println("Cannot get User list");
		}
		_receiver = _username;
		return false;
	}
	
	/**
	 * Close the Connection and unregister the User
	 */
	public void exit() {
		_scanner.close();
		try {
			UnicastRemoteObject.unexportObject(_instance, true);
			_remoteObj.unregister(_username, ServerInterface.CLIENT);
		} catch(Exception ex) {
			if(connect()) {
				exit();
				return;
			}
			System.out.println("Error on exit");
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Send a message
	 * @param message A String that represents the Message
	 */
	public void sendMessage(String message) {
		try {
			_remoteObj.sendMessage(_username, _receiver, message, ServerInterface.CLIENT);
		} catch (RemoteException e) {
			if(connect()) {
				sendMessage(message);
				return;
			}
			System.out.println("Error while sending message");
		}
	}
	
	
	public static void main(String[] args) {
		
		Client client = new Client();
		
		
		boolean connected = client.connect();
		if (connected == false) {
			System.out.println("The Server is not responding. Please try again later!");
			return;
		}
		
		
		if(client.login() == false)
			return;

		
		client.paintUserInterface();
		
		Scanner scanner = new Scanner(System.in);
		String text = "";
		String receiver = null;
		boolean userIsRegistered = false;
		boolean closeSession = false;
	
		
		while(!closeSession) {
			System.out.println("####> ");
			text = scanner.nextLine();
			
			
			if(text.equalsIgnoreCase("list"))
				System.out.println("Users registered: " + Arrays.toString(client.getUserList()));
			
			
			else if(text.equalsIgnoreCase("chuser")) {
				userIsRegistered = false;
				System.out.println(">>>>> Enter User Name to chat with: ");
				receiver = scanner.nextLine();
				userIsRegistered = client.changeUser(receiver);
				if(!userIsRegistered)
					System.out.println("No such user logged in!");
			}
			
			
			else if(text.equalsIgnoreCase("exit")) {
				scanner.close();
				closeSession = true;
				client.exit();
			}
			
			
			else if(userIsRegistered)
				client.sendMessage(text);
			
			
			else {
				System.out.println("Use chuser to select user.");
				userIsRegistered = false;
			}
		}
		
		return;
	}
}
