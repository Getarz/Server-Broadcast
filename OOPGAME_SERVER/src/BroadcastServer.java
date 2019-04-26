import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import javax.swing.JButton;

public class BroadcastServer extends Thread {
	DatagramSocket socket;
	static String[] stack = new String[4];
	int check = 0;
	static boolean checkTrue = false;

	public BroadcastServer() {
		for (int i = 0; i < stack.length; i++) {
			stack[i] = "no";
		}
	}

	@Override
	public void run() {
		try {
			// Keep a socket open to listen to all the UDP trafic that is destined for this
			// port
			socket = new DatagramSocket(9999, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
			while (true) {
				System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");
				byte[] recvBuf = new byte[15000];
				System.out.println("Wait..");
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				System.out.println(getClass().getName() + ">>>Discovery packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));



				String message = new String(packet.getData()).trim();
				String str = packet.getAddress().getHostAddress();
				if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
					for (int i = 0; i < stack.length; i++) {
						if (stack[i].equals(str)) {
							checkTrue = true;
							break;
						} else {
							checkTrue = false;
							System.out.println("Get in " + i + " IP " + packet.getAddress().getHostAddress());
						}
					}
				}
				if (checkTrue == false) {
					System.out.println("Success ! ! !");
					stack[check] = packet.getAddress().getHostAddress();
					check++;
				}
				for (int i = 0; i < stack.length; i++) {
					System.out.println(stack[i]);
				}
				boolean xx = stack[0].equals("no") || stack[1].equals("no") || stack[2].equals("no");
				byte[] sendData = "Welcome".getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
						packet.getPort());
				socket.send(sendPacket);
				System.out.println(
						getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
				if (xx == false) {
					
					System.out.println("Stop ! ! !");
					break;
				}
			}
			try {
				String str = "Ready";
				Socket socket1 = new Socket(stack[0], 50113);
				PrintStream dataOut1 = new PrintStream(socket1.getOutputStream());
				dataOut1.println(str);
				dataOut1.close();

				Socket socket2 = new Socket(stack[1], 50113);
				PrintStream dataOut2 = new PrintStream(socket2.getOutputStream());
				dataOut2.println(str);
				dataOut2.close();

				Socket socket3 = new Socket(stack[2], 50113);
				PrintStream dataOut3 = new PrintStream(socket3.getOutputStream());
				dataOut3.println(str);
				dataOut3.close();
			} catch (Exception e1) {
				System.out.println(">>>>>> " + e1);
			}

		} catch (IOException ex) {

			// Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null,
			// ex);

		}

	}

}
