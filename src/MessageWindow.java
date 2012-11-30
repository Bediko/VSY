import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;


public class MessageWindow extends JFrame {

	private JPanel contentPane;
	private String mReceiver;
	private JTextArea txtConversation, txtMessage;
	private ClientGUI mInterface;


	/**
	 * Create the frame.
	 */
	public MessageWindow(String receiver, ClientGUI inter) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("Konversation mit " + receiver);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				hideWindow();
			}
		});
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtConversation = new JTextArea();
		txtConversation.setEditable(false);
		txtConversation.setBounds(12, 12, 418, 105);
		contentPane.add(txtConversation);
		
		txtMessage = new JTextArea();
		txtMessage.setBounds(12, 127, 418, 88);
		contentPane.add(txtMessage);
		
		mInterface = inter;
		mReceiver = receiver;
		
		JButton btnSendMessage = new JButton("Send Message");
		btnSendMessage.setBounds(12, 227, 418, 25);
		contentPane.add(btnSendMessage);
		btnSendMessage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
			
		});
	}
	
	/**
	 * appends the message to the textarea and sends the message to the receiver
	 */
	public void sendMessage() {
		if(txtMessage.getText().equals(""))
			return;
		mInterface.sendMessage(mReceiver, txtMessage.getText());
		txtConversation.append("Du: " + txtMessage.getText() + "\n");
		txtMessage.setText("");
	}
	
	/**
	 * receives a message and appends it in the textarea
	 * @param message the message to be delivered
	 */
	public void addMessage(String message) {
		txtConversation.append(mReceiver + ": " + message + "\n");
	}
	
	/**
	 * hides the messagewindow
	 */
	public void hideWindow() {
		setVisible(false);
	}
	
}
