
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Stack;

public class ServSentDataClient extends Thread {
	public String[] card = new String[52];
	public Stack stackk = new Stack();
	int[] ranCard = new int[52];

	public ServSentDataClient() {
		int j = 1;
		int c = 1;
		for (int i = 0; i < card.length; i++) {
			card[i] = "" + (j) + "-" + (c);
			System.out.println("card : " + card[i] + " " + i);
			if (c == 4) {
				c = 0;
				j++;
			}
			c++;
		}
		// **** random
//		for (int i = 0; i < card.length; i++) {
//			boolean check = false;
//			int ran = 0;
//			ran = (int) (Math.random()*51);
//			
//			if(i==0) {
//				System.out.println("Success");
//				ranCard[i] = ""+ran;
//				System.out.println(ranCard[i]+" "+i);
//				continue;
//			}
//			for (int k = 0; k < ranCard.length; k++) {
//				String xx = ""+ran;
//				if(ranCard[k].equals(xx)) {
//					check = true;
//					break;
//				}
//			}
//			if(check==false) {
//				ranCard[i] = ""+ran;
//				stackk.push(ranCard);
//				System.out.println(ranCard[i]+" "+i);
//			}else {
//				i--;
//			}
//		}
	}

	public void run() {
		ServerSocket servSoket;
		int check = 0;
		try {
			servSoket = new ServerSocket(50111);
			while (true) {
				try {
					String line = "";
					Socket socket = servSoket.accept();
					InputStream input = socket.getInputStream();
					InputStreamReader inputStream = new InputStreamReader(input);
					BufferedReader bufferIn = new BufferedReader(inputStream);
					System.out.println(socket);

					while ((line = bufferIn.readLine()) != null) {
						String[] str = line.split("-");
						System.out.println("Length of : " + str.length);
						if (str.length == 3) {
							for (int j = 0; j < str.length; j++) {
								if (str[0].equals(BroadcastServer.stack[j])) {
									line = line + "-" + j;
								}
							}
							check++;
							System.out.println("check : " + check);
							sentData(line);
						}
						if (check == 3) {
							for (int i = 0; i < 3; i++) {
								for (int j = 0; j < BroadcastServer.stack.length-1; j++) {
									
									Socket sock = new Socket(BroadcastServer.stack[j], 50113);
									PrintStream dat = new PrintStream(sock.getOutputStream());
									line = "set-"+((int)(Math.random()*51+0));
									System.out.println(line+""+BroadcastServer.stack[j]);
									dat.println(line+""+BroadcastServer.stack[j]);
									dat.close();
								}
							}
						}
						if (str.length == 4 && str[0].equals("chat")) {
							sentData(line);
						}

					}
					bufferIn.close();

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void sentData(String line) {
		try {
			System.out.println(line);
			Socket socket1 = new Socket(BroadcastServer.stack[0], 50113);
			PrintStream dataOut = new PrintStream(socket1.getOutputStream());
			dataOut.println(line);
			dataOut.close();

			System.out.println(line);
			Socket socket2 = new Socket(BroadcastServer.stack[1], 50113);
			PrintStream dataOut2 = new PrintStream(socket2.getOutputStream());
			dataOut2.println(line);
			dataOut2.close();

			Socket socket3 = new Socket(BroadcastServer.stack[2], 50113);
			PrintStream dataOut3 = new PrintStream(socket3.getOutputStream());
			dataOut3.print(line);
			dataOut3.close();

//			Socket socket4 = new Socket(BroadcastServer.stack[3],50113);
//			PrintStream dataOut4 = new PrintStream(socket4.getOutputStream());
//			dataOut4.print(line);
//			dataOut4.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
