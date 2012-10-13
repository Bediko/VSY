/**
 * 
 */
package client;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import server.Client_conn;



public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setSecurityManager (new RMISecurityManager ());
				try {
					Client_conn conn = (Client_conn)
					Naming.lookup("rmi://127.0.0.1:9090/server");
					conn.getMessage(args[0]);
				} catch ( Exception ex ) {
					System.out.println ("Exception:"+ ex.getMessage ());
				}

	}

}
