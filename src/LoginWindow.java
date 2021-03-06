import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;


public class LoginWindow extends JFrame {

	private JPanel contentPane;
	private ClientGUI mInterface;
	private JTextField txtUser, txtPass;
	
	public LoginWindow(ClientGUI clientInterface) {
		mInterface = clientInterface;
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 190, 304);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(12, 55, 158, 19);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(12, 106, 158, 19);
		contentPane.add(txtPass);
		txtPass.setColumns(10);
		
		JButton btnCheckIn = new JButton("Login");
		btnCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mInterface.setUser(txtUser.getText());
				mInterface.setPass(txtPass.getText());
				if(mInterface.login()) {
					close();
				}
				else {
					JOptionPane.showOptionDialog(null, "Der Login war nicht erfolgreich. Mögliche Gründe:\n\n" +
							"- falscher Username\n" +
							"- falsches Passwort\n" +
							"- Sie sind auf einem anderen PC angemeldet\n" +
							"- der Server ist nicht erreichbar", 
							"Fehler beim Login", 
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
				}
			}
		});
		btnCheckIn.setBounds(36, 149, 117, 25);
		contentPane.add(btnCheckIn);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(12, 36, 158, 19);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(12, 89, 158, 15);
		contentPane.add(lblPassword);
		
		JButton btnRegister = new JButton("Create User");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mInterface.setUser(txtUser.getText());
				mInterface.setPass(txtPass.getText());
				if(mInterface.register()) {
					mInterface.login();
					close();
				}
				else {
					JOptionPane.showOptionDialog(null, "Die Registrierung war nicht erfolgreich. Mögliche Gründe:\n\n" +
							"- Username existiert bereits\n" +
							"- der Server ist nicht erreichbar", 
							"Fehler beim Login", 
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);					
				}
					
			}
		});
		btnRegister.setBounds(12, 240, 158, 25);
		contentPane.add(btnRegister);
	}
	
	public void close() {
		setVisible(false);
	}
}
