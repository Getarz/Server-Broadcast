import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class FrameServerBroadcast extends JFrame {
	public JPanel panel = new JPanel(null);
	public TextArea text = new TextArea("",10,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
	public JButton but = new JButton();
	
	public FrameServerBroadcast(){
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
		but.addActionListener(new clickBut(this));
		add(but);
		add(text);
		add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		
	}

}

class clickBut implements ActionListener{
	FrameServerBroadcast frame;
	public clickBut(FrameServerBroadcast frameServerBroadcast) {
		this.frame = frameServerBroadcast;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		BroadcastServer b = new BroadcastServer(frame);
		ServSentDataClient s = new ServSentDataClient(frame);
		s.start();
		b.start();
	}
	
}