import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class UserListWindow extends JFrame {

	private JPanel contentPane;
	private JList<String> mUserList;
	private DefaultListModel<String> mListModel;
	private ClientGUI mInterface;
	
	public UserListWindow(ClientGUI inter) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				mInterface.exit();
				System.exit(0);
			}
		});
		setBounds(100, 100, 166, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mInterface = inter;
		mListModel = new DefaultListModel<String>();
		
		mUserList = new JList<String>();
		mUserList.setBounds(12, 12, 134, 249);
		mUserList.setModel(mListModel);
		mUserList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					int index = mUserList.getSelectedIndex();
					String receiver = mListModel.get(index);
					mInterface.newConversation(receiver);
				}
			}
		});

		contentPane.add(mUserList);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearList();
				hideUserListWindow();
				mInterface.onLogout();
			}
		});
		btnLogout.setBounds(12, 273, 134, 38);
		
		contentPane.add(btnLogout);
	}
	
	/**
	 * hides the UserListWindow in case of logging out
	 */
	public void hideUserListWindow() {
		setVisible(false);
	}
	
	/**
	 * clears the list of online users
	 */
	public void clearList() {
		mListModel.clear();
	}

	/**
	 * updates the list of online users
	 * @param users the users to be shown in the list
	 */
	public void updateList(String[] users) {
		clearList();
		for(String user : users) {
			if(!user.equals(mInterface.getUser()))
				mListModel.addElement(user);
		}
	}
}
