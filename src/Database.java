import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Connects to the database, sends queries and returns the results
 */

public class Database {

	private Connection conn;
	/**
	 * Builds up the database connection
	 * @param url URL to the database
	 * @param user name of database user
	 * @param password password of database user
	 */
	public Database(String url, String user, String password){
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Puts a registered User into the database
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean register(String user, String password){
		PreparedStatement st;
		Integer rs;
		String query;
		query="INSERT INTO people(name,password) VALUES('"+user+"','"+password+"')";
		try {
			st = conn.prepareStatement(query);
			rs = st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
