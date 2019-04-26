import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class FrameServerBroadcast extends JFrame {
	public JPanel panel = new JPanel(null);
	public JTextArea text = new JTextArea();
	public JButton but = new JButton();
	public static void main(String[] args) {
		FrameServerBroadcast frame = new FrameServerBroadcast();

		frame.frame();
		frame.setVisible(true);

	}
	public void frame() {
//		ServSentDataClient s = new ServSentDataClient(this);
//		s.start();
		setTitle("Broadcast Server");
		setSize(400	, 610);
		setLocationRelativeTo(null);
		setResizable(false);
		
		panel.setSize(400, 600);
		panel.setLocation(0,0);
		panel.setBackground(new Color(10000)); 
		
		text.setSize(370, 500);
		text.setLocation(13,10);
		
		but.setSize(100, 50);
		but.setLocation(150, 520);
		but.setText("Start");
		but.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BroadcastServer b = new BroadcastServer();
				b.start();
				
			}
		});
		
		add(but);
		add(text);
		add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
